package com.geekband.huzhouapp.utils;

import android.annotation.SuppressLint;

import com.chat.adapter.pojo.Answer;
import com.chat.adapter.pojo.Expert;
import com.chat.adapter.pojo.Message;
import com.chat.adapter.pojo.Question;
import com.database.dto.DataOperation;
import com.database.pojo.EnquiryTable;
import com.database.pojo.ExpertTable;
import com.database.pojo.ReplyTable;
import com.database.pojo.UserInfoTable;
import com.database.pojo.UserTable;
import com.geekband.huzhouapp.application.MyApplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressLint("SimpleDateFormat")
@SuppressWarnings("unchecked")
public final class DataOperationHelper
{
	private DataOperationHelper()
	{
		
	}
	
	public static ArrayList<Expert> queryExpertList() throws Exception
	{
		ArrayList<Expert> expertList = new ArrayList<>();
		
		//专家Table列表
		List<ExpertTable> expertTableList;
		expertTableList = (List<ExpertTable>) DataOperation.queryTable(ExpertTable.TABLE_NAME);
		if(expertTableList!=null)
		{
			for (ExpertTable expertTable : expertTableList)
			{
				//专家的UserTable信息
				UserTable userData = null;
				//专家的UserInfoTable信息
				UserInfoTable userInfoData = null;
				
				List<UserTable> uList = (List<UserTable>) DataOperation.queryTable(UserTable.TABLE_NAME, UserTable.CONTENTID, expertTable.getField(ExpertTable.FIELD_USERNO));
				if(uList!=null && uList.size()!=0) userData = uList.get(0);
				List<UserInfoTable> uIList = null;
				if(userData!=null) uIList= (List<UserInfoTable>) DataOperation.queryTable(UserInfoTable.TABLE_NAME, UserInfoTable.FIELD_USERID, userData.getContentId());
				if(uIList!=null && uIList.size()!=0) userInfoData = (UserInfoTable) DataOperation.queryTable(UserInfoTable.TABLE_NAME, UserInfoTable.FIELD_USERID, userData.getContentId()).get(0);
				
				expertList.add(new Expert(expertTable, userData, userInfoData));
			}
		}
		
		return expertList;
	}
	
	public static UserTable queryCurrentUser() throws Exception
	{
		return (UserTable) DataOperation.queryTable(UserTable.TABLE_NAME, UserTable.CONTENTID, MyApplication.sSharedPreferences.getString(Constants.AUTO_LOGIN, "")).get(0);
	}
	
	public static ArrayList<Answer> queryQuestionAnswerList(String enquiryTableContentId) throws Exception
	{
		ArrayList<Answer> answerList = new ArrayList<>();
		
		//当前问题的所有首条回答列表
		List<ReplyTable> replyList = (List<ReplyTable>) DataOperation.queryTable(ReplyTable.TABLE_NAME, ReplyTable.FIELD_REPLYTONO, enquiryTableContentId);
		for (ReplyTable replyTable : replyList) //问题的每一条回答
		{
			answerList.add(new Answer((UserTable) DataOperation.queryTable(UserTable.TABLE_NAME, UserTable.CONTENTID, replyTable.getField(ReplyTable.FIELD_USERNO)).get(0), replyTable));
		}
		
		return answerList;
	}
	
	public static ArrayList<Question> queryUserAnswerQuestionList(UserTable user) throws Exception
	{
		ArrayList<Question> questionList = new ArrayList<>();
		
		ArrayList<ReplyTable> replyList = (ArrayList<ReplyTable>) DataOperation.queryTable(ReplyTable.TABLE_NAME, ReplyTable.FIELD_USERNO, user.getContentId());
		for (ReplyTable replyTable : replyList)
		{
			ArrayList<EnquiryTable> question = (ArrayList<EnquiryTable>) DataOperation.queryTable(EnquiryTable.TABLE_NAME, EnquiryTable.CONTENTID, replyTable.getField(ReplyTable.FIELD_REPLYTONO));
			if(question!=null && question.size()!=0)
			{
				ArrayList<Answer> answerList = new ArrayList<>();
				ArrayList<ReplyTable> replyList2 = (ArrayList<ReplyTable>) DataOperation.queryTable(ReplyTable.TABLE_NAME, ReplyTable.FIELD_REPLYTONO, question.get(0).getContentId());
				for (ReplyTable replyTable2 : replyList2)
				{
					answerList.add(new Answer((UserTable) DataOperation.queryTable(UserTable.TABLE_NAME, UserTable.CONTENTID, replyTable2.getField(ReplyTable.FIELD_USERNO)).get(0), replyTable2));
				}
				
				questionList.add(new Question((UserTable) DataOperation.queryTable(UserTable.TABLE_NAME, UserTable.CONTENTID, question.get(0).getField(EnquiryTable.FIELD_USERNO)).get(0), question.get(0), answerList));
			}
		}
		
		return questionList;
	}
	
	public static ArrayList<Question> queryUserAskQuestionList(UserTable user) throws Exception
	{
		ArrayList<Question> questionList = new ArrayList<>();
		
		//当前用户的问题列表
		List<EnquiryTable> askList = (ArrayList<EnquiryTable>)DataOperation.queryTable(EnquiryTable.TABLE_NAME, EnquiryTable.FIELD_USERNO, user.getContentId()); //用户提问
		
		//把问题列表数据添加到listView的数据源中
		if(askList!=null)
		{
			for (EnquiryTable ask : askList) //每一条问题
			{
				List<ReplyTable> replyList = (List<ReplyTable>) DataOperation.queryTable(ReplyTable.TABLE_NAME, ReplyTable.FIELD_REPLYTONO, ask.getContentId());
				List<Answer> answerList = new ArrayList<>();
				
				if(replyList!=null)
				{
					for (ReplyTable replyTable : replyList) //问题的每一条回答
					{
						answerList.add(new Answer((UserTable) DataOperation.queryTable(UserTable.TABLE_NAME, UserTable.CONTENTID, replyTable.getField(ReplyTable.FIELD_USERNO)).get(0), replyTable));
					}
				}
				
				questionList.add(new Question(user, ask, answerList));
			}
		}
		
		return questionList;
	}
	
	public static ArrayList<Message> queryChildMessageList(Message headMessage) throws Exception
	{
		ArrayList<Message> messageList = new ArrayList<>();
		
		if(headMessage!=null)
		{
			messageList.add(new Message(headMessage.getMessageSenderInfo(), headMessage.getMessageInfo(), headMessage.getMessageSenderInfo().equals(queryCurrentUser())?Message.TYPE_SEND_TEXT:Message.TYPE_RECEIVE_TEXT));
			String replyContentId = headMessage.getMessageInfo().getContentId();
			ArrayList<ReplyTable> nextReply = (ArrayList<ReplyTable>) DataOperation.queryTable(ReplyTable.TABLE_NAME, ReplyTable.FIELD_REPLYTONO, replyContentId);
			while(nextReply!=null && nextReply.size()!=0)
			{
				messageList.add(new Message(
						(UserTable) DataOperation.queryTable(UserTable.TABLE_NAME, UserTable.CONTENTID, nextReply.get(0).getField(ReplyTable.FIELD_USERNO)).get(0),
						nextReply.get(0), 
						DataOperation.queryTable(UserTable.TABLE_NAME, UserTable.CONTENTID, nextReply.get(0).getField(ReplyTable.FIELD_USERNO)).get(0).equals(queryCurrentUser())?Message.TYPE_SEND_TEXT:Message.TYPE_RECEIVE_TEXT));
				
				replyContentId = nextReply.get(0).getContentId();
				nextReply = (ArrayList<ReplyTable>) DataOperation.queryTable(ReplyTable.TABLE_NAME, ReplyTable.FIELD_REPLYTONO, replyContentId);
			}
		}
		
		return messageList;
	}
	
	/**
	 * 
	 * @param sender 消息发送者
	 * @param replyToNo 消息发送的对象的contentId
	 * @param content 消息内容
	 * @param time 时间
	 * @return
	 * @throws Exception
	 */
	public static Message uploadMessage(UserTable sender, String replyToNo, String content, String time) throws Exception
	{
		Message message = null;
		
		//创建ReplyTable(代表一条回复)
		ReplyTable reply = new ReplyTable();
		reply.putField(ReplyTable.FIELD_USERNO, sender.getContentId());
		reply.putField(ReplyTable.FIELD_CONTENT, content);
		reply.putField(ReplyTable.FIELD_T_TIME, time);
		reply.putField(ReplyTable.FIELD_REPLYTONO, replyToNo);
		
		//将新建的ReplyTable数据上传到服务器数据库中
		if(DataOperation.insertOrUpdateTable(reply, null))
		{
			message = new Message(sender, reply, Message.TYPE_SEND_TEXT);
		}
		
		return message;
	}
	
	public static Question uploadQuestion(UserTable asker, String content, String time, String category) throws Exception
	{
		Question question = null;
		
		//创建EnquiryTable(代表一个问题)
		EnquiryTable enquiryData = new EnquiryTable();
		enquiryData.putField(EnquiryTable.FIELD_USERNO, asker.getContentId());
		enquiryData.putField(EnquiryTable.FIELD_CONTENT, content);
		enquiryData.putField(EnquiryTable.FIELD_APPLYTIME, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(System.currentTimeMillis())));
		enquiryData.putField(EnquiryTable.FIELD_CATEGORYID, category);
		
		//将新建的EnquiryTable数据上传到服务器
		if(DataOperation.insertOrUpdateTable(enquiryData, null))
		{
			question = new Question(asker, enquiryData, null);
		}
		
		return question;
	}
	
}
