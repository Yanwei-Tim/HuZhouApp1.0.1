package com.chat.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.chat.activity.QuestionDetailActivity;
import com.chat.adapter.OthersQuestionListAdapter;
import com.chat.adapter.pojo.Answer;
import com.chat.adapter.pojo.Question;
import com.database.dto.DataOperation;
import com.database.pojo.EnquiryTable;
import com.database.pojo.ReplyTable;
import com.database.pojo.UserTable;
import com.geekband.huzhouapp.R;

import java.util.ArrayList;
import java.util.List;

public class OthersQuestionFragment extends Fragment
{
	private View v_rootView;
	private ListView lv_questionListView;
	private ViewGroup vg_progress;
	
	private OthersQuestionListAdapter othersQuestionListAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		v_rootView = inflater.inflate(R.layout.fragment_othersquestion, container, false);
		
		findView();
		initVar();
		initView();
		initListener();
		
		return v_rootView;
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		
		runAsyncTask(AsyncDataLoader.TASK_INIT);
	}
	
	private void findView()
	{
		lv_questionListView = (ListView) v_rootView.findViewById(R.id.lv_othersquestion_questionList);
		vg_progress = (ViewGroup) v_rootView.findViewById(R.id.vg_othersquestion_progress);
	}
	
	private void initView()
	{
		lv_questionListView.setAdapter(othersQuestionListAdapter);
	}
	
	private void initVar()
	{
		othersQuestionListAdapter = new OthersQuestionListAdapter(getActivity(), lv_questionListView);
	}
	
	private void initListener()
	{
		lv_questionListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Intent intent = new Intent();
				intent.setClass(getActivity(), QuestionDetailActivity.class);
				intent.putExtra(QuestionDetailActivity.ARGS_QUESTION, othersQuestionListAdapter.getItem(position));
				startActivity(intent);
			}
		});
	}
	
	private void runAsyncTask(int task, Object... params)
	{
		new AsyncDataLoader(task, params).execute();
	}
	
	private class AsyncDataLoader extends AsyncTask<Object, Integer, Integer>
	{
		private static final int TASK_INIT = 1;
		private static final int TASK_INIT_RESULT_SUCCESS = 1;
		private static final int TASK_INIT_RESULT_ERROR = -1;
		
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
			switch (task)
			{
				case TASK_INIT:
				{
					vg_progress.setVisibility(View.VISIBLE);
					lv_questionListView.setVisibility(View.GONE);
				}break;
			}
		}
		
		@Override
		protected Integer doInBackground(Object... params)
		{
			switch (task)
			{
				case TASK_INIT:
				{
					try
					{
						List<EnquiryTable> askList = (List<EnquiryTable>) DataOperation.queryTable(EnquiryTable.TABLE_NAME);
						
						if(askList!=null)
						{
							othersQuestionListAdapter.getQuestionList().clear();
							for (EnquiryTable ask : askList) //每一条问题
							{
								//List<ReplyTable> replyList = DataOperation.queryReplyTableList(ask.getContentId());
								List<ReplyTable> replyList = (List<ReplyTable>) DataOperation.queryTable(ReplyTable.TABLE_NAME, ReplyTable.FIELD_REPLYTONO, ask.getContentId());
								List<Answer> answerList = new ArrayList<>();
								
								if(replyList!=null)
								{
									for (ReplyTable replyTable : replyList) //问题的每一条回答
									{
										//answerList.add(new Answer(DataOperation.queryUserTableFromContentId(replyTable.getField(ReplyTable.FIELD_USERNO)), replyTable));
										answerList.add(new Answer((UserTable) DataOperation.queryTable(UserTable.TABLE_NAME, UserTable.CONTENTID, replyTable.getField(ReplyTable.FIELD_USERNO)).get(0), replyTable));
									}
								}
								
								//othersQuestionListAdapter.getQuestionList().add(new Question(DataOperation.queryUserTableFromContentId(ask.getField(EnquiryTable.FIELD_USERNO)), ask, answerList));
								othersQuestionListAdapter.getQuestionList().add(new Question((UserTable) DataOperation.queryTable(UserTable.TABLE_NAME, UserTable.CONTENTID, ask.getField(EnquiryTable.FIELD_USERNO)).get(0), ask, answerList));
							}
						}

						return TASK_INIT_RESULT_SUCCESS;
					}
					catch (Exception e)
					{
						e.printStackTrace();
						return TASK_INIT_RESULT_ERROR;
					}
				}
			}
			
			return 0;
		}
		
		@Override
		protected void onPostExecute(Integer taskResult)
		{
			switch (taskResult)
			{
				case TASK_INIT_RESULT_SUCCESS:
				{
					vg_progress.setVisibility(View.GONE);
					lv_questionListView.setVisibility(View.VISIBLE);
					othersQuestionListAdapter.notifyDataSetChanged();
				}break;
				
				case TASK_INIT_RESULT_ERROR:
				{
					vg_progress.setVisibility(View.GONE);
					lv_questionListView.setVisibility(View.VISIBLE);
					Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
				}break;
			}
		}
	}
}
