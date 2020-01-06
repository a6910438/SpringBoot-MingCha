package com.xhr.mca.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.xhr.mca.common.CoreMapper;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.User;
import com.xhr.mca.entity.UserTeams;

public interface UserTeamsMapper extends CoreMapper<UserTeams> {

	@Insert("insert into user_teams(user_id, team_ids) SELECT id,null FROM user where id not in (SELECT user_id from user_teams)")
	public void insertTeamUser() throws WebAppException;

	@Select("select * from user_teams where user_id = #{id} ")
	public UserTeams selectById(@Param(value = "id") Long id) throws WebAppException;

	@Select("select DISTINCT(u.phone),u.email,u.img_url as imgUrl,u.nickname,u.level,u.node_level as nodeLevel from `user` u left join user_auth ua on u.id = ua.user_id where u.id in (${ids}) and (ua.`status` = 1) limit #{page},#{rows}")
	public List<User> selectTeamAuthAdpotUsers(@Param(value = "ids") String ids, @Param(value = "page") int page,
			@Param(value = "rows") int rows) throws WebAppException;

	@Select("select DISTINCT(u.phone),u.email,u.img_url as imgUrl,u.nickname,u.level,u.node_level as nodeLevel from `user` u where u.id in (${ids}) limit #{page},#{rows}")
	public List<User> selectTeamAllUsers(@Param(value = "ids") String ids, @Param(value = "page") int page,
			@Param(value = "rows") int rows) throws WebAppException;

	@Select("select DISTINCT(u.phone),u.email,u.img_url as imgUrl,u.nickname,u.level,u.node_level as nodeLevel from `user` u left join user_auth ua on u.id = ua.user_id where u.id in (${ids}) and (ua.`status` != 1 or ua.user_id is null) limit #{page},#{rows}")
	public List<User> selectTeamAuthUsers(@Param(value = "ids") String ids, @Param(value = "page") int page,
			@Param(value = "rows") int rows) throws WebAppException;

}
