package com.xhr.mca.service;

import java.io.IOException;
import java.util.List;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.User;
import com.xhr.mca.entity.vo.Team;

public interface UserService {

	public User login(User user) throws WebAppException;

	public void register(User user) throws WebAppException;

	public void forgot(User user) throws WebAppException;

	public User getUserById(long id) throws WebAppException;

	public void update(User user) throws WebAppException, IOException;

	public void updateLoginPass(User user) throws WebAppException;

	public void updatePayPass(User user) throws WebAppException;

	public int getChildTeamNum(long userId, Integer num) throws WebAppException;

	public List<Team> getChildTeam(long userId, List<Team> teams) throws WebAppException;

	public List<User> getChildsByUserId(long pid) throws WebAppException;

	public int getInviteNum(long userId) throws WebAppException;

	public User getParentSuperNodeByUserId(Long userId, User u) throws WebAppException;

	public void updateUserTeams() throws WebAppException;

	public void checkPayPassword(long userId, String password) throws WebAppException;

	public List<User> getUsersByIds(String ids) throws WebAppException;

	public User getLevelSuperNodeByUserId(Long userId) throws WebAppException;

}
