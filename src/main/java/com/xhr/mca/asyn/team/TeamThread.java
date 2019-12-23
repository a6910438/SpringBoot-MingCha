package com.xhr.mca.asyn.team;

import com.xhr.mca.entity.Config;
import com.xhr.mca.entity.User;
import com.xhr.mca.entity.constant.ConfigKeys;
import com.xhr.mca.entity.constant.Node;
import com.xhr.mca.mapper.ConfigMapper;
import com.xhr.mca.service.UserService;

import lombok.extern.log4j.Log4j;

@Log4j
public class TeamThread extends Thread {

	private UserService userService;
	private ConfigMapper configMapper;
	private Long parentId;

	public TeamThread(UserService userService, Long parentId, ConfigMapper configMapper) {
		this.userService = userService;
		this.parentId = parentId;
		this.configMapper = configMapper;
	}

	@Override
	public void run() {
		try {
			int num = userService.getChildTeamNum(parentId, 0);
			Config team = configMapper.selectOne(new Config(ConfigKeys.SUPER_NODE_TEAM_NUMBER.getKey()));
			Config invite = configMapper.selectOne(new Config(ConfigKeys.SUPER_NODE_INVITE_NUMBER.getKey()));
			if (team != null) {
				if (num >= Integer.valueOf(team.getValue())) {
					int inviteNum = userService.getInviteNum(parentId);
					if (inviteNum >= Integer.valueOf(invite.getValue())) {
						User u = new User();
						u.setId(parentId);
						u.setNodeLevel(Node.High.ordinal());
						userService.update(u);
					}

				}
			}
		} catch (Exception e) {
			log.error("TeamThread run error:", e);
		}
	}

}
