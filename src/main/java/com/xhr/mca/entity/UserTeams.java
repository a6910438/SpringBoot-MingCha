package com.xhr.mca.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 用户团队表
 * 
 * @author xhr
 *
 */
@Data
@Table(name = "user_teams")
public class UserTeams {

	@Id
	@Column(name = "user_id")
	private Long userId;

	/** 团队成员集合 **/
	@Column(name = "team_ids")
	private String teamIds;

}
