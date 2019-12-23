package com.xhr.mca.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.xhr.mca.common.CoreMapper;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.User;

public interface UserMapper extends CoreMapper<User> {

	@Select("select * from user where phone = #{phone} and areacode = #{areacode} and password1 = #{password}")
	public User login(@Param(value = "phone") String phone, @Param(value = "areacode") String areacode,
			@Param(value = "password") String password) throws WebAppException;
}
