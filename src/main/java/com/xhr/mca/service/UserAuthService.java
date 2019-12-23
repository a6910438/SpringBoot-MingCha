package com.xhr.mca.service;

import java.io.IOException;
import java.util.List;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.UserAuth;
import com.xhr.mca.entity.constant.AuthStatus;

public interface UserAuthService {

	public void insert(UserAuth userAuth) throws WebAppException, IOException;

	public UserAuth findUserAuthById(Long id) throws WebAppException;

	public void update(UserAuth userAuth) throws WebAppException;

	public List<UserAuth> findAll() throws WebAppException;

	public UserAuth findUserAuthByUserId(Long userId) throws WebAppException;

	public AuthStatus getStatusByUserId(Long userId) throws WebAppException;

}
