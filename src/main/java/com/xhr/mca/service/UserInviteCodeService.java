package com.xhr.mca.service;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.User;
import com.xhr.mca.entity.UserInviteCode;
import com.xhr.mca.entity.vo.Teams;

public interface UserInviteCodeService {

	public void insert(UserInviteCode uic) throws WebAppException;

	public UserInviteCode findInviteCodeByUserId(Long userId) throws WebAppException;

	public Teams findTeamsByUser(User user) throws WebAppException;

}
