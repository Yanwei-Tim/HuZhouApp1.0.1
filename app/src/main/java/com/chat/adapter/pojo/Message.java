package com.chat.adapter.pojo;

import com.database.pojo.ReplyTable;
import com.database.pojo.UserTable;

import java.io.Serializable;


public class Message implements Serializable
{
	private static final long serialVersionUID = 5922976569956649690L;
	
	private UserTable messageSenderInfo;
	private ReplyTable messageInfo;
	private int messageType;
	
	public Message(UserTable messageSenderInfo, ReplyTable messageInfo, int messageType)
	{
		setMessageSenderInfo(messageSenderInfo);
		setMessageInfo(messageInfo);
		setMessageType(messageType);
	}
	
	public UserTable getMessageSenderInfo()
	{
		return messageSenderInfo;
	}
	public void setMessageSenderInfo(UserTable messageSenderInfo)
	{
		this.messageSenderInfo = messageSenderInfo;
	}
	public ReplyTable getMessageInfo()
	{
		return messageInfo;
	}
	public void setMessageInfo(ReplyTable messageInfo)
	{
		this.messageInfo = messageInfo;
	}
	public int getMessageType()
	{
		return messageType;
	}
	public void setMessageType(int messageType)
	{
		this.messageType = messageType;
	}

	public static final int TYPE_SEND_TEXT = 0;
	public static final int TYPE_RECEIVE_TEXT = 1;
}
