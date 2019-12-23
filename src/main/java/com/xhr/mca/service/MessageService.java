package com.xhr.mca.service;

import java.util.List;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.Message;

public interface MessageService {

	public void insert(Message message) throws WebAppException;

	public List<Message> selectAll(Message message) throws WebAppException;

	public void update(Message message) throws WebAppException;

}
