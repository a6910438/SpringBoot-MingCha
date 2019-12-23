package com.xhr.mca.asyn.team;

import com.xhr.mca.service.UserService;

import lombok.extern.log4j.Log4j;

@Log4j
public class TeamUpdateThread extends Thread {

	private UserService userService;

	public TeamUpdateThread(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void run() {
		try {
			userService.updateUserTeams();
		} catch (Exception e) {
			log.error("TeamUpdateThread run error:", e);
		}
	}

}
