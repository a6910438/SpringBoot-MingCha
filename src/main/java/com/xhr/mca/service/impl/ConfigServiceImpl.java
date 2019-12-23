package com.xhr.mca.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhr.mca.common.Utility;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.Config;
import com.xhr.mca.entity.Region;
import com.xhr.mca.entity.User;
import com.xhr.mca.entity.UserInviteCode;
import com.xhr.mca.entity.constant.ConfigKeys;
import com.xhr.mca.entity.vo.DownloadVo;
import com.xhr.mca.mapper.ConfigMapper;
import com.xhr.mca.mapper.RegionMapper;
import com.xhr.mca.mapper.UserInviteCodeMapper;
import com.xhr.mca.redis.UserManagent;
import com.xhr.mca.service.ConfigService;

@Service
public class ConfigServiceImpl implements ConfigService {

	private final ConfigMapper configMapper;
	private final RegionMapper regionMapper;
	private final UserManagent userManagent;
	private final UserInviteCodeMapper uicMapper;

	@Autowired
	public ConfigServiceImpl(ConfigMapper configMapper, RegionMapper regionMapper, UserManagent userManagent,
			UserInviteCodeMapper uicMapper) {
		this.configMapper = configMapper;
		this.regionMapper = regionMapper;
		this.userManagent = userManagent;
		this.uicMapper = uicMapper;
	}

	@Override
	@Transactional
	public void insert(Config config) throws WebAppException {
		configMapper.insert(config);
	}

	@Override
	@Transactional
	public void update(Config config) throws WebAppException {
		configMapper.updateByPrimaryKeySelective(config);
	}

	@Override
	public List<Config> selectAll(Config config) throws WebAppException {
		return configMapper.select(config);
	}

	@Override
	public Config findConfigByKey(String key) throws WebAppException {
		return configMapper.selectOne(new Config(key));
	}

	@Override
	public List<Region> findRegionsByParentId(Long parentId) throws WebAppException {
		return regionMapper.select(new Region(parentId));
	}

	@Override
	public DownloadVo findAndoridDownload() throws WebAppException {
		DownloadVo vo = new DownloadVo();
		Config c = findConfigByKey(ConfigKeys.ANDROID_DOWNLOAD_URL.getKey());
		if (Utility.isNotNull(c)) {
			vo.setQrcodeUrl(c.getValue());
		}
		User u = userManagent.getCurrentUser();
		vo.setNickname(u.getNickname());
		vo.setHeadImageUrl(u.getImgUrl());

		UserInviteCode uic = uicMapper.selectOne(new UserInviteCode(u.getId()));
		if (Utility.isNotNull(uic)) {
			vo.setInviteCode(uic.getInviteCode());
		}
		return vo;
	}

}
