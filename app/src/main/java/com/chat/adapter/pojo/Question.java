package com.chat.adapter.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.database.pojo.EnquiryTable;
import com.database.pojo.ReplyTable;
import com.database.pojo.UserTable;

/**
 * 
 * 问题
 * 
 */
public class Question implements Serializable
{
	private static final long serialVersionUID = 108060571825664663L;
	
	private UserTable askerInfo; //提问者信息
	private EnquiryTable askInfo; //提问信息
	private List<Answer> answerInfoList = new ArrayList<>(); //回答信息
	
	public Question(UserTable askerInfo, EnquiryTable askInfo, List<Answer> answerInfoList)
	{
		setAskerInfo(askerInfo);
		setAskInfo(askInfo);
		if(answerInfoList!=null) getAnswerInfoList().addAll(answerInfoList);
	}

	public UserTable getAskerInfo()
	{
		return askerInfo;
	}
	public void setAskerInfo(UserTable askerInfo)
	{
		this.askerInfo = askerInfo;
	}
	public EnquiryTable getAskInfo()
	{
		return askInfo;
	}
	public void setAskInfo(EnquiryTable askInfo)
	{
		this.askInfo = askInfo;
	}
	public List<Answer> getAnswerInfoList()
	{
		return answerInfoList;
	}
}
