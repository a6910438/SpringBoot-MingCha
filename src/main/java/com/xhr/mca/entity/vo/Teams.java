package com.xhr.mca.entity.vo;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.xhr.mca.entity.User;

import lombok.Data;

/**
 * 我的团队
 * 
 * @author xhr
 *
 */
@Data
public class Teams {

	/** 推荐码 **/
	private String inviteCode;
	/** 团队人数 **/
	private int teamnum;
	/** 我的推荐人 **/
	private String name;
	
	private PageInfo<User> pTeam;
	
	/** 团队集合 **/
	private List<User> list;

}
