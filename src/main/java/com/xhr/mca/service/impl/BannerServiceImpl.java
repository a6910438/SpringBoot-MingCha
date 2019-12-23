package com.xhr.mca.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.Banner;
import com.xhr.mca.mapper.BannerMapper;
import com.xhr.mca.service.BannerService;

@Service
public class BannerServiceImpl implements BannerService {

	@Autowired
	private BannerMapper bannerMapper;

	@Override
	@Transactional
	public void insert(Banner banner) throws WebAppException {
		bannerMapper.insert(banner);
	}

	@Override
	@Transactional
	public void update(Banner banner) throws WebAppException {
		bannerMapper.updateByPrimaryKeySelective(banner);
	}

	@Override
	public List<Banner> selectAll(Banner banner) throws WebAppException {
		return bannerMapper.select(banner);
	}

}
