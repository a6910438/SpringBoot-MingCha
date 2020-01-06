package com.xhr.mca.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhr.mca.asyn.address.EthAddressThread;
import com.xhr.mca.asyn.sms.MessageThread;
import com.xhr.mca.asyn.team.TeamThread;
import com.xhr.mca.asyn.team.TeamUpdateThread;
import com.xhr.mca.common.Constants;
import com.xhr.mca.common.MD5;
import com.xhr.mca.common.Utility;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.config.AliyunOss;
import com.xhr.mca.config.HttpSender;
import com.xhr.mca.entity.Config;
import com.xhr.mca.entity.User;
import com.xhr.mca.entity.UserInviteCode;
import com.xhr.mca.entity.UserTeams;
import com.xhr.mca.entity.constant.ConfigKeys;
import com.xhr.mca.entity.constant.ExceptionConstants;
import com.xhr.mca.entity.constant.Node;
import com.xhr.mca.entity.constant.Sex;
import com.xhr.mca.entity.constant.SmsTemplate;
import com.xhr.mca.entity.constant.Status;
import com.xhr.mca.entity.vo.Team;
import com.xhr.mca.mapper.AddressMapper;
import com.xhr.mca.mapper.CoinMapper;
import com.xhr.mca.mapper.ConfigMapper;
import com.xhr.mca.mapper.UserAssestMapper;
import com.xhr.mca.mapper.UserInviteCodeMapper;
import com.xhr.mca.mapper.UserMapper;
import com.xhr.mca.mapper.UserTeamsMapper;
import com.xhr.mca.redis.RedisCache;
import com.xhr.mca.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;
	private final RedisCache<String, String> redis;
	private final ConfigMapper configMapper;
	private final UserInviteCodeMapper uicMapper;
	private final HttpSender httpSender;
	private final AliyunOss aliyunOss;
	private final UserAssestMapper userAssestMapper;
	private final AddressMapper addressMapper;
	private final CoinMapper coinMapper;
	private final UserTeamsMapper userTeamsMapper;

	@Autowired
	public UserServiceImpl(UserMapper userMapper, RedisCache<String, String> redis, ConfigMapper configMapper,
			UserInviteCodeMapper uicMapper, HttpSender httpSender, AliyunOss aliyunOss,
			UserAssestMapper userAssestMapper, AddressMapper addressMapper, CoinMapper coinMapper,
			UserTeamsMapper userTeamsMapper) {
		this.userMapper = userMapper;
		this.redis = redis;
		this.configMapper = configMapper;
		this.uicMapper = uicMapper;
		this.httpSender = httpSender;
		this.aliyunOss = aliyunOss;
		this.userAssestMapper = userAssestMapper;
		this.addressMapper = addressMapper;
		this.coinMapper = coinMapper;
		this.userTeamsMapper = userTeamsMapper;
	}

	@Override
	public User login(User user) throws WebAppException {
		user.setPassword1(MD5.getMD5(user.getPassword1()));
		User u = userMapper.login(user.getPhone(), user.getAreacode(), user.getPassword1());
		if (u == null) {
			throw new WebAppException(ExceptionConstants.USER_LOGIN_FAILD);
		}

		// 登录成功后走下面逻辑
		u.setLastIp(user.getLastIp());
		u.setUpdateTime(Utility.currentTimestamp());
		userMapper.updateByPrimaryKey(u);

		u.setPassword1("");
		u.setPassword2("");

		// 设置Token放入到Redis里面
		String token = MD5.getMD516(u.getId() + "_" + Utility.currentTimestamp());
		redis.set(token, String.valueOf(u.getId()));
		u.setToken(token);

		if (u.getPid() != null) {
			User parent = userMapper.selectByPrimaryKey(u.getPid());
			if (parent != null) {
				u.setInviteName(parent.getNickname());
				u.setInvitePhone(parent.getPhone());
			}
		}

		return u;
	}

	@Override
	@Transactional
	public void register(User user) throws WebAppException {
		Object verCode = redis.get(user.getAreacode() + user.getPhone() + Constants.REGIST_SUFFIX);
		// 验证码错误
		if (verCode == null || !user.getVerCode().equals(verCode.toString())) {
			throw new WebAppException(ExceptionConstants.VER_CODE_ERROR);
		}

		// 手机号存在
		if (userMapper.selectOne(new User(user.getPhone())) != null) {
			throw new WebAppException(ExceptionConstants.USER_IS_EXIST);
		}

		Config c = new Config(ConfigKeys.RECOMMENDATION_CODE_MUST_BE_FILLED_IN.getKey());
		Long pid = null;
		// 推荐码必须填写的话就进入
		if ("false".equals(configMapper.selectOne(c).getValue())) {
			if (user.getInviteCode() == null || ("").equals(user.getInviteCode())) {
				throw new WebAppException(ExceptionConstants.INVITE_PERSON_NOTEXIST);
			}
			UserInviteCode uic = uicMapper.selectOne(new UserInviteCode(user.getInviteCode()));
			if (uic == null) {
				throw new WebAppException(ExceptionConstants.INVITE_PERSON_NOTEXIST);
			}
			User u = userMapper.selectOne(new User(uic.getPhone()));
			pid = uic.getUserId();
			u.setCountCode(u.getCountCode() + 1);
			userMapper.updateByPrimaryKey(u);
			user.setPid(u.getId());
		}

		user.setCreateTime(Utility.currentTimestamp());
		user.setIsIdcard(Status.NO.ordinal());
		user.setLevel(Node.General.ordinal());
		user.setPassword1(MD5.getMD5(user.getPassword1()));
		user.setPassword2(MD5.getMD5(user.getPassword2()));
		user.setStatus(Status.YES.ordinal()); // 默认启用
		user.setNodeLevel(Node.General.ordinal());
		user.setSex(Sex.Man.ordinal());
		user.setCountCode(0);
		userMapper.insert(user);

		// 判断生成出来的推荐码在数据库是否存在
		String inviteCode = Utility.getRandomString(Constants.SIX);
		if (uicMapper.selectOne(new UserInviteCode(inviteCode)) != null) {
			throw new WebAppException(ExceptionConstants.USER_IS_EXIST);
		}

		// 不存在直接注册进来
		uicMapper.insert(new UserInviteCode(user.getPhone(), inviteCode, Utility.currentTimestamp(), user.getId()));
		redis.del(user.getAreacode() + user.getPhone() + Constants.REGIST_SUFFIX);

		// 绑定ETH地址
		new EthAddressThread(addressMapper, coinMapper, userAssestMapper, user.getId()).start();
		// 发送注册成功短信验证码
		new MessageThread(user.getAreacode(), user.getPhone(), Constants.ZERO, SmsTemplate.CONGRATULATIONS_REGISTER,
				httpSender).start();
		if (pid != null) {
			new TeamThread(this, pid, configMapper).start();
		}
		new TeamUpdateThread(this).start();
	}

	@Override
	public User getUserById(long id) throws WebAppException {
		return userMapper.selectByPrimaryKey(id);
	}

	@Override
	@Transactional
	public void forgot(User user) throws WebAppException {
		Object verCode = redis.get(user.getAreacode() + user.getPhone() + Constants.FORGOT_SUFFIX);
		// 验证码错误
		if (verCode == null || !user.getVerCode().equals(verCode.toString())) {
			throw new WebAppException(ExceptionConstants.VER_CODE_ERROR);
		}

		User u = userMapper.selectOne(new User(user.getPhone()));
		// 手机号不存在
		if (u == null) {
			throw new WebAppException(ExceptionConstants.USER_NOT_EXIST);
		}

		// 更新密码
		u.setPassword1(MD5.getMD5(user.getPassword1()));
		u.setUpdateTime(Utility.currentTimestamp());
		userMapper.updateByPrimaryKey(u);

		// 发送注册成功短信验证码
		new MessageThread(user.getAreacode(), user.getPhone(), Constants.ZERO,
				SmsTemplate.LOGIN_PASSWORD_UPDATE_SUCCESS, httpSender).start();
	}

	@Override
	@Transactional
	public void update(User user) throws WebAppException, IOException {
		// 判断是否有更换头像
		if (user.getFile() != null) {
			aliyunOss.uploadImage(user.getFile(), Constants.USER_IMAGE_PATH, user.getId().toString());
			user.setImgUrl(aliyunOss.getUrl() + "/" + Constants.USER_IMAGE_PATH + user.getId().toString()
					+ Constants.PNG_SUFFIX);
		}
		if (!StringUtils.isBlank(user.getBase64FileString())) {
			aliyunOss.uploadImageBase64(user.getBase64FileString(), Constants.USER_IMAGE_PATH, user.getId().toString());
			user.setImgUrl(aliyunOss.getUrl() + "/" + Constants.USER_IMAGE_PATH + user.getId().toString()
					+ Constants.PNG_SUFFIX);
		}
		userMapper.updateByPrimaryKeySelective(user);
	}

	@Override
	@Transactional
	public void updateLoginPass(User user) throws WebAppException {
		Object verCode = redis.get(user.getAreacode() + user.getPhone() + Constants.UPTPASS_SUFFIX);
		// 验证码错误
		if (verCode == null || !user.getVerCode().equals(verCode.toString())) {
			throw new WebAppException(ExceptionConstants.VER_CODE_ERROR);
		}

		User u = userMapper.selectOne(new User(user.getPhone()));
		// 手机号不存在
		if (u == null) {
			throw new WebAppException(ExceptionConstants.USER_NOT_EXIST);
		}

		// 更新密码
		u.setPassword1(MD5.getMD5(user.getPassword1()));
		u.setUpdateTime(Utility.currentTimestamp());
		userMapper.updateByPrimaryKey(u);
		redis.del(user.getAreacode() + user.getPhone() + Constants.UPTPASS_SUFFIX);

		// 发送注册成功短信验证码
		new MessageThread(user.getAreacode(), user.getPhone(), Constants.ZERO,
				SmsTemplate.LOGIN_PASSWORD_UPDATE_SUCCESS, httpSender).start();
	}

	@Override
	public void updatePayPass(User user) throws WebAppException {
		Object verCode = redis.get(user.getAreacode() + user.getPhone() + Constants.PAYPASS_SUFFIX);
		// 验证码错误
		if (verCode == null || !user.getVerCode().equals(verCode.toString())) {
			throw new WebAppException(ExceptionConstants.VER_CODE_ERROR);
		}

		User u = userMapper.selectOne(new User(user.getPhone()));
		// 手机号不存在
		if (u == null) {
			throw new WebAppException(ExceptionConstants.USER_NOT_EXIST);
		}

		// 更新密码
		u.setPassword2(MD5.getMD5(user.getPassword2()));
		u.setUpdateTime(Utility.currentTimestamp());
		userMapper.updateByPrimaryKey(u);
		redis.del(user.getAreacode() + user.getPhone() + Constants.PAYPASS_SUFFIX);

		// 发送注册成功短信验证码
		new MessageThread(user.getAreacode(), user.getPhone(), Constants.ZERO, SmsTemplate.PAY_PASSWORD_UPDATE_SUCCESS,
				httpSender).start();
	}

	@Override
	public int getChildTeamNum(long userId, Integer num) throws WebAppException {
		List<User> us = getChildsByUserId(userId);
		num = num + us.size();
		for (User user : us) {
			num = getChildTeamNum(user.getId(), num);
		}
		return num;
	}

	@Override
	public List<Team> getChildTeam(long userId, List<Team> teams) throws WebAppException {
		List<User> users = getChildsByUserId(userId);
		if (users != null) {
			if (teams == null) {
				teams = new ArrayList<Team>();
			}
			for (User user : users) {
				Team t = new Team(user.getImgUrl(), user.getPhone(), user.getPhone(), user.getId());
				teams.add(t);
				teams = getChildTeam(user.getId(), teams);
			}
		} else {
			return new ArrayList<Team>();
		}
		return teams;
	}

	@Override
	public List<User> getChildsByUserId(long userId) throws WebAppException {
		return userMapper.select(new User(userId));
	}

	@Override
	public int getInviteNum(long userId) throws WebAppException {
		return userMapper.selectCount(new User(userId));
	}

	@Override
	public User getParentSuperNodeByUserId(Long userId, User u) throws WebAppException {
		User user = getUserById(userId);
		if (user == null) {
			if (u.getNodeLevel() == Node.High.ordinal()) {
				return u;
			}
		} else {
			return getParentSuperNodeByUserId(user.getPid(), user);
		}
		return null;
	}

	@Override
	public void updateUserTeams() throws WebAppException {
		// UserTeams数据和User数据保持同步
		userTeamsMapper.insertTeamUser();

		// 更新团队
		List<UserTeams> userTeams = userTeamsMapper.select(null);

		for (UserTeams ut : userTeams) {
			List<Team> teams = getChildTeam(ut.getUserId(), null);
			String userIds = "";
			for (Team team : teams) {
				userIds += team.getUserId() + ",";
			}
			if (userIds.length() > 0) {
				userIds = userIds.substring(0, userIds.length() - 1);
			}
			ut.setTeamIds(userIds);
			userTeamsMapper.updateByPrimaryKeySelective(ut);
		}
	}

	@Override
	public void checkPayPassword(long userId, String password) throws WebAppException {
		User u = new User();
		u.setId(userId);
		u.setPassword2(MD5.getMD532(password));
		User user = userMapper.selectOne(u);
		if (Utility.isNull(user)) {
			throw new WebAppException(ExceptionConstants.PAY_PASSWORD_FAILD);
		}
	}

	@Override
	public List<User> getUsersByIds(String ids) throws WebAppException {
		return userMapper.selectByIds(ids);
	}

	@Override
	public User getLevelSuperNodeByUserId(Long userId) throws WebAppException {
		User user = getUserById(userId);
		if (user.getNodeLevel() == Node.High.ordinal()) {
			return user;
		} else {
			return getParentSuperNodeByUserId(user.getPid(), user);
		}
	}
}
