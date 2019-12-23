package com.xhr.mca.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.xhr.mca.common.Result;
import com.xhr.mca.common.ResultUtil;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.User;
import com.xhr.mca.entity.vo.Teams;
import com.xhr.mca.redis.UserManagent;
import com.xhr.mca.service.UserInviteCodeService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/team")
@Log4j
public class TeamController {

	private final UserManagent userManagent;
	private final UserInviteCodeService userInviteCodeService;

	@Autowired
	public TeamController(UserManagent userManagent, UserInviteCodeService userInviteCodeService) {
		this.userManagent = userManagent;
		this.userInviteCodeService = userInviteCodeService;
	}

	/**
	 * 获取团队所有成员
	 * 
	 * @return
	 */
	@RequestMapping(value = "/all", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result info2(@RequestParam(value = "status") Integer status, @RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows) {
		try {
			User u = userManagent.getCurrentUser();
			PageHelper.startPage(page, rows);
			u.setCardAuditStatus(status);
			u.setPage(page);
			u.setRows(rows);
			Teams t = userInviteCodeService.findTeamsByUser(u);
			return ResultUtil.success(t);
		} catch (WebAppException e) {
			log.error("UserController info 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
	}

}
