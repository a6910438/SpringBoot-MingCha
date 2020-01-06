package com.xhr.mca.service;

import java.util.List;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.UserAssest;
import com.xhr.mca.entity.vo.TransactionDetail;
import com.xhr.mca.entity.vo.TransactionVo;
import com.xhr.mca.entity.vo.UserAssestVo;

public interface UserAssestService {

	public UserAssestVo findAssestsByUserId(Long userID) throws WebAppException;

	public void addAssest(UserAssest userAssest) throws WebAppException;

	public UserAssest findAssestByUserIdAndCoinId(Long userID, Long coinID) throws WebAppException;

	public void updateAssest(UserAssest userAssest) throws WebAppException;

	public List<TransactionVo> findTransactionsByUserIdAndCoinId(Long userID, Long coinID, Integer type, Integer page,
			Integer rows) throws WebAppException;

	public TransactionDetail findTransactionDetailById(Long id, Integer type) throws WebAppException;
}
