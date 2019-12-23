package com.xhr.mca.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.xhr.mca.common.CoreMapper;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.UserAuth;

public interface UserAuthMapper extends CoreMapper<UserAuth> {

	@Select("SELECT * FROM user_auth where user_id = #{user_id} ORDER BY create_time DESC LIMIT 1")
	public UserAuth selectOneByUserId(@Param(value = "user_id") Long userId) throws WebAppException;

}
