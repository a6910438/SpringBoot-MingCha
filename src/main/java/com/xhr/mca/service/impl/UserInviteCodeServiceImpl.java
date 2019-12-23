package com.xhr.mca.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhr.mca.common.Constants;
import com.xhr.mca.common.Utility;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.User;
import com.xhr.mca.entity.UserInviteCode;
import com.xhr.mca.entity.UserTeams;
import com.xhr.mca.entity.vo.Teams;
import com.xhr.mca.mapper.UserInviteCodeMapper;
import com.xhr.mca.mapper.UserTeamsMapper;
import com.xhr.mca.service.UserInviteCodeService;
import com.xhr.mca.service.UserService;

@Service
public class UserInviteCodeServiceImpl implements UserInviteCodeService {

	private final UserService userService;
	private final UserInviteCodeMapper userInviteCodeMapper;
	private final UserTeamsMapper userTeamsMapper;

	@Autowired
	public UserInviteCodeServiceImpl(UserInviteCodeMapper userInviteCodeMapper, UserService userService,
			UserTeamsMapper userTeamsMapper) {
		this.userInviteCodeMapper = userInviteCodeMapper;
		this.userService = userService;
		this.userTeamsMapper = userTeamsMapper;
	}

	@Override
	@Transactional
	public void insert(UserInviteCode uic) throws WebAppException {
		userInviteCodeMapper.insert(uic);
	}

	@Override
	public UserInviteCode findInviteCodeByUserId(Long userId) throws WebAppException {
		return userInviteCodeMapper.selectOne(new UserInviteCode(userId));
	}

	@Override
	public Teams findTeamsByUser(User user) throws WebAppException {
		Teams teams = new Teams();
		// 获取推荐人名称
		User u = userService.getUserById(user.getPid());
		teams.setName(u == null ? user.getNickname() : u.getNickname());

		// 获取团队人数
		int teamnum = userService.getChildTeamNum(user.getId(), Constants.ZERO);
		teams.setTeamnum(teamnum);

		List<User> users = new ArrayList<User>();
		UserTeams uts = userTeamsMapper.selectById(user.getId());
		if (Utility.isNotNull(uts) && !StringUtils.isBlank(uts.getTeamIds())) {
			String ids = uts.getTeamIds();
			// 未实名
			if (user.getCardAuditStatus() == -1) {
				users = userTeamsMapper.selectTeamAuthUsers(ids, (user.getPage() - 1) * user.getRows(), user.getRows());
			} else if (user.getCardAuditStatus() == 0) {
				// 查全部
				users = userTeamsMapper.selectTeamAllUsers(ids, (user.getPage() - 1) * user.getRows(), user.getRows());
			} else {
				// 通过
				users = userTeamsMapper.selectTeamAuthAdpotUsers(ids, (user.getPage() - 1) * user.getRows(),
						user.getRows());
			}
		}
		teams.setList(users);

		UserInviteCode uic = findInviteCodeByUserId(user.getId());
		teams.setInviteCode(uic.getInviteCode());

		return teams;
	}

}
