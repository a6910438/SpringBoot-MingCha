package com.xhr.mca.service;

import java.util.List;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.Config;
import com.xhr.mca.entity.Region;
import com.xhr.mca.entity.vo.DownloadVo;

public interface ConfigService {

	public void insert(Config config) throws WebAppException;

	public void update(Config config) throws WebAppException;

	public List<Config> selectAll(Config config) throws WebAppException;

	public Config findConfigByKey(String key) throws WebAppException;

	public List<Region> findRegionsByParentId(Long parentId) throws WebAppException;
	
	public DownloadVo findAndoridDownload() throws WebAppException;

}
