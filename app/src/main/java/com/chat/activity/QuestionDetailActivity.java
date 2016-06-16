package com.chat.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chat.adapter.QuestionDetailListAdapter;
import com.chat.adapter.pojo.Answer;
import com.chat.adapter.pojo.Message;
import com.chat.adapter.pojo.Question;
import com.database.dto.DataOperation;
import com.database.pojo.ReplyTable;
import com.database.pojo.UserTable;
import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.application.MyApplication;
import com.geekband.huzhouapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class QuestionDetailActivity extends BaseActivity implements View.OnClickListener
{
	private ImageButton btn_back;
	private TextView tv_title;
	private ListView lv_questionDetailListView;
	private Button btn_answer;
	private ViewGroup vg_detail; //包含lv_questionDetailListView
	private ViewGroup vg_progress;
	private ViewGroup vg_answer;
	
	private QuestionDetailListAdapter questionDetailListAdapter;
	
	@Override
	protected void onCreate(Bundle saveInstanceState)
	{
		super.onCreate(saveInstanceState);
		
		setContentView(R.layout.activity_questiondetail);
		findView();
		initVar();
		initView();
		initListener();
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		
		runAsyncTask(AsyncDataLoader.TASK_UPDATELISTVIEW); //刷新ListView
	}
	
	private void findView()
	{
		btn_back = (ImageButton) findViewById(R.id.btn_questiondetail_back);
		tv_title = (TextView) findViewById(R.id.tv_questiondetail_title);
		lv_questionDetailListView = (ListView) findViewById(R.id.lv_questiondetail_questionList);
		btn_answer = (Button) findViewById(R.id.btn_questiondetail_answer);
		vg_detail = (ViewGroup) findViewById(R.id.vg_questiondetail_detail);
		vg_progress = (ViewGroup) findViewById(R.id.vg_questiondetail_progress);
		vg_answer = (ViewGroup) findViewById(R.id.vg_questiondetail_answer);
	}
	
	private void initView()
	{
		tv_title.setText(questionDetailListAdapter.getQuestion().getAskerInfo().getField(UserTable.FIELD_USERNAME)+"的提问"); //设置标题
		lv_questionDetailListView.setAdapter(questionDetailListAdapter); //设置问答列表
		if(questionDetailListAdapter.getQuestion().getAskerInfo().getField(UserTable.FIELD_USERNAME).equals(MyApplication.sSharedPreferences.getString(Constants.AUTO_LOGIN, ""))) vg_answer.setVisibility(View.GONE); //判断用户名
		else vg_answer.setVisibility(View.VISIBLE);
	}
	
	private void initVar()
	{
		Bundle args = getIntent().getExtras();
		if(args!=null)
		{
			Question question = (Question) args.getSerializable(ARGS_QUESTION);
			questionDetailListAdapter = new QuestionDetailListAdapter(this, lv_questionDetailListView, question);
		}
	}
	
	private void initListener()
	{
		btn_back.setOnClickListener(this);
		btn_answer.setOnClickListener(this);
		
		lv_questionDetailListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) //查看每条回答的详情
			{
				if(position!=0)
				{
					Answer answer = questionDetailListAdapter.getQuestion().getAnswerInfoList().get(position-1);
					Message message = new Message(answer.getReplierInfo(), answer.getAnswerInfo(), Message.TYPE_RECEIVE_TEXT);
					List<UserTable> userList = new ArrayList<>();
					userList.add(answer.getReplierInfo());
					userList.add(questionDetailListAdapter.getQuestion().getAskerInfo());
					
					Intent intent = new Intent();
					intent.setClass(QuestionDetailActivity.this, ChatActivity.class);
					intent.putExtra(ChatActivity.ARGS_MESSAGE, message);
					intent.putExtra(ChatActivity.ARGS_USERLIST, (ArrayList<UserTable>)userList);
					intent.putExtra(ChatActivity.ARGS_TITLE, questionDetailListAdapter.getQuestion().getAskerInfo().getField(UserTable.FIELD_USERNAME)+"的提问");
					startActivity(intent);
				}
			}
		});
	}
	
	private void runAsyncTask(int task, Object... params)
	{
		new AsyncDataLoader(task, params).execute();
	}

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.btn_questiondetail_back:
			{
				finish();
			}break;
			
			case R.id.btn_questiondetail_answer: //回答提问
			{
				runAsyncTask(AsyncDataLoader.TASK_ANSWER);
			}break;
		}
	}
	
	private class AsyncDataLoader extends AsyncTask<Object, Integer, Integer>
	{
		private static final int TASK_ANSWER = 1;
		private static final int TASK_ANSWER_RESULT_SUCCESS = 1;
		private static final int TASK_ANSWER_RESULT_ERROR = -1;
		private static final int TASK_UPDATELISTVIEW = 2;
		private static final int TASK_UPDATELISTVIEW_RESULT_SUCCESS = 2;
		private static final int TASK_UPDATELISTVIEW_RESULT_ERROR = -2;
		
		private int task;
		private Object[] params;
		
		public AsyncDataLoader(int task, Object... params)
		{
			this.task = task;
			this.params = params;
		}
		
		@Override
		protected void onPreExecute()
		{
			switch(task)
			{
				case TASK_ANSWER:
				{
					
				}break;
				
				case TASK_UPDATELISTVIEW:
				{
					vg_progress.setVisibility(View.VISIBLE);
					vg_detail.setVisibility(View.GONE);
				}break;
			}
		}
		
		UserTable currentUser;
		@Override
		protected Integer doInBackground(Object... params)
		{
			switch(task)
			{
				case TASK_ANSWER:
				{
					try
					{
						UserTable currentUser = (UserTable) DataOperation.queryTable(UserTable.TABLE_NAME, UserTable.CONTENTID, MyApplication.sSharedPreferences.getString(Constants.AUTO_LOGIN, "")).get(0);
						
						//找到当前用户对当前问题的第一条回答
						//当前用户对当前问题的第一条回答
						Message firstMessage = null;
						//当前问题的所有首条回答列表
						List<ReplyTable> replyList = (List<ReplyTable>) DataOperation.queryTable(ReplyTable.TABLE_NAME, ReplyTable.FIELD_REPLYTONO, questionDetailListAdapter.getQuestion().getAskInfo().getContentId());
						if(replyList!=null)
						{
							for (ReplyTable reply : replyList)
							{
								//判断回答列表中是否有当前用户
								if(currentUser.equals(DataOperation.queryTable(UserTable.TABLE_NAME, UserTable.CONTENTID, reply.getField(ReplyTable.FIELD_USERNO)).get(0)))
								{
									//列表中有当前用户的回答
									firstMessage = new Message(currentUser, reply, Message.TYPE_SEND_TEXT);
								}
							}
						}
						
						//参与聊天的用户列表
						List<UserTable> userList = new ArrayList<>();
						UserTable asker = questionDetailListAdapter.getQuestion().getAskerInfo();
						userList.add(currentUser);
						userList.add(asker);
						
						//启动ChatActivity
						Intent intent = new Intent();
						intent.setClass(QuestionDetailActivity.this, ChatActivity.class);
						if(firstMessage!=null) intent.putExtra(ChatActivity.ARGS_MESSAGE, firstMessage);
						else intent.putExtra(ChatActivity.ARGS_REPLYNO, questionDetailListAdapter.getQuestion().getAskInfo().getContentId());
						intent.putExtra(ChatActivity.ARGS_USERLIST, (ArrayList<UserTable>)userList);
						intent.putExtra(ChatActivity.ARGS_TITLE, questionDetailListAdapter.getQuestion().getAskerInfo().getField(UserTable.FIELD_USERNAME)+"的提问");
						startActivity(intent);
						
						return TASK_ANSWER_RESULT_SUCCESS;
					}
					catch (Exception e)
					{
						e.printStackTrace();
						return TASK_ANSWER_RESULT_ERROR;
					}
				}
				
				case TASK_UPDATELISTVIEW:
				{
					try
					{
						//当前用户UserTable
						currentUser = (UserTable) DataOperation.queryTable(UserTable.TABLE_NAME, UserTable.CONTENTID, MyApplication.sSharedPreferences.getString(Constants.AUTO_LOGIN, "")).get(0);
						//更新当前回答的 回答列表(重新从服务器上读取回答列表数据)
						List<ReplyTable> replyList;
						replyList = (List<ReplyTable>) DataOperation.queryTable(ReplyTable.TABLE_NAME, ReplyTable.FIELD_REPLYTONO, questionDetailListAdapter.getQuestion().getAskInfo().getContentId());
						if(replyList!=null)
						{
							questionDetailListAdapter.getQuestion().getAnswerInfoList().clear();
							for (ReplyTable replyTable : replyList) //问题的每一条回答
							{
								questionDetailListAdapter.getQuestion().getAnswerInfoList().add(new Answer((UserTable) DataOperation.queryTable(UserTable.TABLE_NAME, UserTable.CONTENTID, replyTable.getField(ReplyTable.FIELD_USERNO)).get(0), replyTable));
							}
						}
						
						return TASK_UPDATELISTVIEW_RESULT_SUCCESS;
					}
					catch (Exception e)
					{
						e.printStackTrace();
						return TASK_UPDATELISTVIEW_RESULT_ERROR;
					}
				}
			}
			
			return 0;
		}
		
		@Override
		protected void onPostExecute(Integer taskResult)
		{
			switch(taskResult)
			{
				case TASK_ANSWER_RESULT_SUCCESS:
				{
					
				}break;
				
				case TASK_ANSWER_RESULT_ERROR:
				{
					Toast.makeText(QuestionDetailActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
				}break;
				
				case TASK_UPDATELISTVIEW_RESULT_SUCCESS:
				{
					questionDetailListAdapter.notifyDataSetChanged();
					vg_progress.setVisibility(View.GONE);
					vg_detail.setVisibility(View.VISIBLE);
					if(currentUser!=null && !currentUser.equals(questionDetailListAdapter.getQuestion().getAskerInfo()) ) vg_answer.setVisibility(View.VISIBLE);
					else vg_answer.setVisibility(View.GONE);
				}break;
				
				case TASK_UPDATELISTVIEW_RESULT_ERROR:
				{
					vg_progress.setVisibility(View.GONE);
					vg_detail.setVisibility(View.VISIBLE);
					Toast.makeText(QuestionDetailActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
				}break;
			}
		}
	}
	
	public static final String ARGS_QUESTION = "QUESTION";
}
