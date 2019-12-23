package com.xhr.mca.service;

import java.util.List;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.Banner;

public interface BannerService {

	public void insert(Banner banner) throws WebAppException;
	
	public void update(Banner banner) throws WebAppException;
	
	public List<Banner> selectAll(Banner banner) throws WebAppException;
	
}
