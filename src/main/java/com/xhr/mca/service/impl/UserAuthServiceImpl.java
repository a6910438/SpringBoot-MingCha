package com.xhr.mca.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhr.mca.common.Constants;
import com.xhr.mca.common.Utility;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.UserAuth;
import com.xhr.mca.entity.constant.AuditStatus;
import com.xhr.mca.entity.constant.AuthStatus;
import com.xhr.mca.http.AliyunOss;
import com.xhr.mca.mapper.UserAuthMapper;
import com.xhr.mca.service.UserAuthService;

@Service
public class UserAuthServiceImpl implements UserAuthService {

	private final UserAuthMapper userAuthMapper;
	private final AliyunOss aliyunOss;

	@Autowired
	public UserAuthServiceImpl(UserAuthMapper userAuthMapper, AliyunOss aliyunOss) {
		this.userAuthMapper = userAuthMapper;
		this.aliyunOss = aliyunOss;
	}

	@Override
	@Transactional
	public void insert(UserAuth userAuth) throws WebAppException, IOException {
		long time = Utility.currentTimestamp();
		// 上传阿里云 并配置字段URL路径
		aliyunOss.uploadImageBase64(userAuth.getBackImgFile(), Constants.IDCARD_IMG_PATH + Constants.ONE + "/",
				String.valueOf(time));
		aliyunOss.uploadImageBase64(userAuth.getFrontImgFile(), Constants.IDCARD_IMG_PATH + Constants.TWO + "/",
				String.valueOf(time));
		aliyunOss.uploadImageBase64(userAuth.getFaceImgFile(), Constants.IDCARD_IMG_PATH + Constants.THREE + "/",
				String.valueOf(time));
		userAuth.setBackImgUrl(aliyunOss.getUrl() + "/" + Constants.IDCARD_IMG_PATH + Constants.ONE + "/"
				+ time + Constants.PNG_SUFFIX);
		userAuth.setFrontImgUrl(aliyunOss.getUrl() + "/" + Constants.IDCARD_IMG_PATH + Constants.TWO + "/"
				+ time + Constants.PNG_SUFFIX);
		userAuth.setFaceImgUrl(aliyunOss.getUrl() + "/" + Constants.IDCARD_IMG_PATH + Constants.THREE + "/"
				+ time + Constants.PNG_SUFFIX);
		// 创建时间
		userAuth.setCreateTime(Utility.currentTimestamp());
		userAuth.setStatus(AuditStatus.Audit.ordinal());

		// 录入到数据库
		userAuthMapper.insert(userAuth);
	}

	@Override
	public UserAuth findUserAuthById(Long id) throws WebAppException {
		return userAuthMapper.selectByPrimaryKey(id);
	}

	@Override
	@Transactional
	public void update(UserAuth userAuth) throws WebAppException {
		userAuthMapper.updateByPrimaryKeySelective(userAuth);
	}

	@Override
	public List<UserAuth> findAll() throws WebAppException {
		return userAuthMapper.selectAll();
	}

	@Override
	public UserAuth findUserAuthByUserId(Long userId) throws WebAppException {
		return userAuthMapper.selectOneByUserId(userId);
	}

	@Override
	public AuthStatus getStatusByUserId(Long userId) throws WebAppException {
		UserAuth ua = findUserAuthByUserId(userId);
		if (Utility.isNotNull(ua)) {
			if (ua.getStatus() == AuditStatus.Audit.ordinal()) {
				return AuthStatus.UPLOAD;
			} else if (ua.getStatus() == AuditStatus.Adopt.ordinal()) {
				return AuthStatus.PASS;
			} else {
				return AuthStatus.NO_PASS;
			}
		}
		return AuthStatus.NO_UPLOAD;
	}

}
