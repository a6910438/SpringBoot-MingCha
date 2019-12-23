package com.xhr.mca.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.Message;
import com.xhr.mca.mapper.MessageMapper;
import com.xhr.mca.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

	private final MessageMapper messageMapper;

	@Autowired
	public MessageServiceImpl(MessageMapper messageMapper) {
		this.messageMapper = messageMapper;
	}

	@Override
	@Transactional
	public void insert(Message message) throws WebAppException {
		messageMapper.insert(message);
	}

	@Override
	public List<Message> selectAll(Message message) throws WebAppException {
		return messageMapper.select(message);
	}

	@Override
	@Transactional
	public void update(Message message) throws WebAppException {
		messageMapper.updateByPrimaryKeySelective(message);
	}

}
