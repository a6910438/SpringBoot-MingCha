package com.xhr.mca.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Numeric;

import com.alibaba.fastjson.JSON;
import com.xhr.mca.common.Constants;
import com.xhr.mca.common.MD5;
import com.xhr.mca.common.Utility;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.Coin;
import com.xhr.mca.entity.Config;
import com.xhr.mca.entity.McaRecord;
import com.xhr.mca.entity.OldRecord;
import com.xhr.mca.entity.RmbRecord;
import com.xhr.mca.entity.SunRecord;
import com.xhr.mca.entity.User;
import com.xhr.mca.entity.UserAssest;
import com.xhr.mca.entity.Withdraw;
import com.xhr.mca.entity.constant.AuditStatus;
import com.xhr.mca.entity.constant.ConfigKeys;
import com.xhr.mca.entity.constant.ExceptionConstants;
import com.xhr.mca.mapper.CoinMapper;
import com.xhr.mca.mapper.McaRecordMapper;
import com.xhr.mca.mapper.OldRecordMapper;
import com.xhr.mca.mapper.RmbRecordMapper;
import com.xhr.mca.mapper.SunRecordMapper;
import com.xhr.mca.mapper.UserMapper;
import com.xhr.mca.mapper.WithdrawMapper;
import com.xhr.mca.redis.RedisCache;
import com.xhr.mca.service.ConfigService;
import com.xhr.mca.service.TransferService;
import com.xhr.mca.service.UserAssestService;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class TransferServiceImpl implements TransferService {

	private final WithdrawMapper withdrawMapper;
	private final Web3j web3j;
	private final RedisCache<String, String> redis;
	private final CoinMapper coinMapper;
	private final ConfigService configService;
	private final UserAssestService userAssestService;
	private final Credentials credentials;
	private final UserMapper userMapper;
	private final McaRecordMapper mcaRecordMapper;
	private final SunRecordMapper sunRecordMapper;
	private final RmbRecordMapper rmbRecordMapper;
	private final OldRecordMapper oldRecordMapper;

	@Autowired
	public TransferServiceImpl(WithdrawMapper withdrawMapper, Web3j web3j, CoinMapper coinMapper,
			ConfigService configService, UserAssestService userAssestService, Credentials credentials,
			RedisCache<String, String> redis, UserMapper userMapper, McaRecordMapper mcaRecordMapper,
			SunRecordMapper sunRecordMapper, RmbRecordMapper rmbRecordMapper, OldRecordMapper oldRecordMapper) {
		this.withdrawMapper = withdrawMapper;
		this.web3j = web3j;
		this.coinMapper = coinMapper;
		this.configService = configService;
		this.userAssestService = userAssestService;
		this.credentials = credentials;
		this.redis = redis;
		this.userMapper = userMapper;
		this.mcaRecordMapper = mcaRecordMapper;
		this.sunRecordMapper = sunRecordMapper;
		this.rmbRecordMapper = rmbRecordMapper;
		this.oldRecordMapper = oldRecordMapper;
	}

	@Override
	@Transactional
	public boolean sendTransaction(long coinId, long userId, String to, BigDecimal number, String payPassword,
			String code) throws WebAppException, Exception {
		User user = new User();
		user.setId(userId);
		user.setPassword2(MD5.getMD532(payPassword));
		User u = userMapper.selectOne(user);
		if (u == null) {
			throw new WebAppException(ExceptionConstants.PAY_PASSWORD_ERROR);
		}

		Object verCode = redis.get(u.getAreacode() + u.getPhone() + Constants.WITHDRAW_SUFFIX);
		// 验证码错误
		if (verCode == null || !code.equals(verCode.toString())) {
			throw new WebAppException(ExceptionConstants.VER_CODE_ERROR);
		}

		UserAssest ua = userAssestService.findAssestByUserIdAndCoinId(userId, coinId);
		Coin coin = coinMapper.selectByPrimaryKey(coinId);
		BigDecimal commission = number.multiply(new BigDecimal(coin.getFee()));
		BigDecimal finalNumber = commission.add(number);
		if (ua == null || ua.getAvaBalance().compareTo(finalNumber) < 0) {
			throw new WebAppException(ExceptionConstants.INSUFFICIENT_BALANCE);
		}

		Config config = configService.findConfigByKey(ConfigKeys.THAN_WITHDRAW_NUMBER.getKey());
		if (number.compareTo(new BigDecimal(config.getValue())) >= 0) {
			Withdraw withdraw = new Withdraw(null, userId, to, null, number, commission, AuditStatus.Audit.ordinal(),
					Utility.currentTimestamp(), null, coinId);
			withdrawMapper.insert(withdraw);

			ua.setAvaBalance(ua.getAvaBalance().subtract(finalNumber));
			userAssestService.updateAssest(ua);
			return false;
		}

		String txid = transferToken(credentials.getAddress(), to, coin.getContractAddress(), number);
		Withdraw withdraw = new Withdraw(null, userId, to, txid, number, commission, AuditStatus.Adopt.ordinal(),
				Utility.currentTimestamp(), Utility.currentTimestamp(), coinId);
		withdrawMapper.insert(withdraw);

		ua.setAvaBalance(ua.getAvaBalance().subtract(finalNumber));
		userAssestService.updateAssest(ua);

		return true;
	}

	public BigInteger getTransactionGasLimit(Transaction transaction) {
		try {
			EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(transaction).send();
			if (ethEstimateGas.hasError()) {
				throw new RuntimeException(ethEstimateGas.getError().getMessage());
			}
			return ethEstimateGas.getAmountUsed();
		} catch (IOException e) {
			throw new RuntimeException("net error");
		}
	}

	public String transferToken(String fromAddr, String toAddr, String contractAddr, BigDecimal amount)
			throws InterruptedException, ExecutionException, IOException, CipherException {

		EthGetTransactionCount ethGetTransactionCount = web3j
				.ethGetTransactionCount(fromAddr, DefaultBlockParameterName.LATEST).sendAsync().get();
		BigInteger nonce = ethGetTransactionCount.getTransactionCount();
		// 构建方法调用信息
		String method = "transfer";

		BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();

		// 构建输入参数
		List<Type> inputArgs = new ArrayList<>();
		inputArgs.add(new Address(toAddr));
		inputArgs.add(new Uint256(amount.multiply(BigDecimal.TEN.pow(18)).toBigInteger()));
		// 合约返回值容器
		List<TypeReference<?>> outputArgs = new ArrayList<>();

		String funcABI = FunctionEncoder.encode(new Function(method, inputArgs, outputArgs));

		Transaction transaction = Transaction.createFunctionCallTransaction(fromAddr, nonce, gasPrice, null,
				contractAddr, funcABI);

		BigInteger gasLimit = getTransactionGasLimit(transaction);

		return signAndSend(nonce, gasPrice, gasLimit, contractAddr, BigInteger.ZERO, funcABI);
	}

	public String signAndSend(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value,
			String data) throws IOException, CipherException {
		String txHash = "";
		RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, data);

		byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);

		String signData = Numeric.toHexString(signMessage);
		if (!"".equals(signData)) {
			try {
				EthSendTransaction send = web3j.ethSendRawTransaction(signData).send();
				txHash = send.getTransactionHash();
				System.out.println(JSON.toJSONString(send));
			} catch (IOException e) {
				throw new RuntimeException("交易异常");
			}
		}
		return txHash;
	}

	@Override
	@Transactional
	public void sendSystemTransaction(long coinId, long userId, String phone, BigDecimal number, String payPassword,
			String code) throws WebAppException, Exception {
		User user = new User();
		user.setId(userId);
		user.setPassword2(MD5.getMD532(payPassword));
		User u = userMapper.selectOne(user);
		if (u == null) {
			throw new WebAppException(ExceptionConstants.PAY_PASSWORD_ERROR);
		}

		Object verCode = redis.get(u.getAreacode() + u.getPhone() + Constants.WITHDRAW_SUFFIX);
		// 验证码错误
		if (verCode == null || !code.equals(verCode.toString())) {
			throw new WebAppException(ExceptionConstants.VER_CODE_ERROR);
		}

		UserAssest ua = userAssestService.findAssestByUserIdAndCoinId(userId, coinId);
		if (ua == null || ua.getAvaBalance().compareTo(number) < 0) {
			throw new WebAppException(ExceptionConstants.INSUFFICIENT_BALANCE);
		}
		BigDecimal uaOld = ua.getAvaBalance();
		User recevie = userMapper.selectOne(new User(phone));
		if (recevie == null) {
			throw new WebAppException(ExceptionConstants.USER_NOT_EXIST);
		}

		// 资产更新
		UserAssest recevieua = userAssestService.findAssestByUserIdAndCoinId(recevie.getId(), coinId);
		ua.setAvaBalance(ua.getAvaBalance().subtract(number));
		userAssestService.updateAssest(ua);
		recevieua.setAvaBalance(recevieua.getAvaBalance().add(number));
		userAssestService.updateAssest(recevieua);

		// 流水添加
		Coin coin = coinMapper.selectByPrimaryKey(coinId);
		if (Constants.MCA.equals(coin.getName())) {
			McaRecord mca = new McaRecord(null, userId, com.xhr.mca.entity.constant.Type.TRANSFER.getId(), uaOld,
					ua.getAvaBalance(), number, recevie.getId(), Utility.currentTimestamp(), null);
			mcaRecordMapper.insert(mca);
		} else if (Constants.SUN.equals(coin.getName())) {
			SunRecord sun = new SunRecord(null, userId, com.xhr.mca.entity.constant.Type.TRANSFER.getId(), uaOld,
					ua.getAvaBalance(), number, recevie.getId(), Utility.currentTimestamp(), null);
			sunRecordMapper.insert(sun);
		} else if (Constants.OLD.equals(coin.getName())) {
			OldRecord old = new OldRecord(null, userId, com.xhr.mca.entity.constant.Type.TRANSFER.getId(), uaOld,
					ua.getAvaBalance(), number, recevie.getId(), Utility.currentTimestamp(), null);
			oldRecordMapper.insert(old);
		} else if (Constants.RMB.equals(coin.getName())) {
			RmbRecord rmb = new RmbRecord(null, userId, com.xhr.mca.entity.constant.Type.TRANSFER.getId(), uaOld,
					ua.getAvaBalance(), number, recevie.getId(), Utility.currentTimestamp(), null);
			rmbRecordMapper.insert(rmb);
		}
	}

}
