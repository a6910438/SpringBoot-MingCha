package com.xhr.mca.cron;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import com.xhr.mca.common.Constants;
import com.xhr.mca.common.Contract;
import com.xhr.mca.common.EthConvert;
import com.xhr.mca.common.EthConvert.Unit;
import com.xhr.mca.common.Utility;
import com.xhr.mca.entity.EthAddress;
import com.xhr.mca.entity.constant.Status;
import com.xhr.mca.entity.vo.Payment;
import com.xhr.mca.service.AddressService;

import lombok.extern.log4j.Log4j;

@Component
@Log4j
public class EthTask {

	@Value("${eth.node.url}")
	private String ethRpcUrl;
	@Value("${eth.privateKey.folder}")
	private String folder;

	private final AddressService addressService;

	@Autowired
	public EthTask(AddressService addressService) {
		this.addressService = addressService;
	}

	@Scheduled(cron = "0 */1 * * * ?")
	public void execute() {
		/**
		 * 生成ETH钱包地址
		 */
		try {
			if (!addressService.addressThanCondition()) {
				EthAddress address = new EthAddress();
				address.setIsUse(Status.NO.ordinal());
				address.setCreateTime(Utility.currentTimestamp());
				String publicKey = createNewWallet();
				if (addressService.selectCountByAddress(new EthAddress(publicKey)) == 0) {
					address.setAddress(publicKey);
					addressService.insert(address);
				}
			}
		} catch (Exception e) {
			log.error("EthTask execute 发送异常:", e);
		}
	}

	public Web3j build() {
		return Web3j.build(new HttpService(ethRpcUrl));
	}

	public String createNewWallet() throws NoSuchAlgorithmException, NoSuchProviderException,
			InvalidAlgorithmParameterException, CipherException, IOException, CipherException {
		String fileName = WalletUtils.generateNewWalletFile(Constants.ETH_PASSWORD, new File(folder), true);
		Credentials credentials = WalletUtils.loadCredentials(Constants.ETH_PASSWORD,
				new File(folder) + "/" + fileName);
		String address = credentials.getAddress();
		return address;
	}

	public void transferToken(Payment payment) {
		Contract contract = new Contract();
		try {
			Web3j web3j = build();
			System.out.println(web3j.ethGetBalance("0xea674fdde714fd979de3edf0f56aa9716b898ec8",
					DefaultBlockParameter.valueOf("latest")).send().getBalance());
			contract.setAddress("0xe385f25d70a57b0b7dafdffca713eb68d94887f8");
			EthGetTransactionCount ethGetTransactionCount = web3j
					.ethGetTransactionCount(payment.getCredentials().getAddress(), DefaultBlockParameterName.LATEST)
					.sendAsync().get();
			BigInteger nonce = ethGetTransactionCount.getTransactionCount();
			BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
			BigInteger value = EthConvert.toWei(payment.getAmount(), Unit.WWEI).toBigInteger();
			Function fn = new Function("transfer",
					Arrays.asList(new org.web3j.abi.datatypes.Address(payment.getTo()), new Uint256(value)),
					Collections.<TypeReference<?>>emptyList());
			String data = FunctionEncoder.encode(fn);
			BigInteger maxGas = new BigInteger("4500");
			RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, maxGas,
					contract.getAddress(), data);
			byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, payment.getCredentials());
			String hexValue = Numeric.toHexString(signedMessage);
			log.info("hexRawValue={}" + hexValue);
			EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
			String transactionHash = ethSendTransaction.getTransactionHash();
			log.info("txid:" + transactionHash);
			if (StringUtils.isEmpty(transactionHash)) {
				return;
			} else {
				web3j.ethSendRawTransaction(hexValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException,
			InvalidAlgorithmParameterException, CipherException, IOException {
		Credentials credentials = WalletUtils.loadCredentials("",
				"D:/testeth/UTC--2019-12-06T07-46-38.720000000Z--f8251ab6210606c3e06f8429b90ba946b4c5c94c.json");
		Payment payment = Payment.builder().credentials(credentials).amount(new BigDecimal("10"))
				.to("0xddF3D8fe538C249e592C89ed91Fa103Dd3d2A9af").build();
		transferToken(payment);
//		System.out.println(createNewWallet("", ""));
	}

}
