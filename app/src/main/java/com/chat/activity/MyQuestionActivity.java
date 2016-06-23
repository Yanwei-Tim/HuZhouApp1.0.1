package com.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.chat.adapter.pojo.Question;
import com.database.pojo.EnquiryTable;
import com.database.pojo.UserTable;
import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.custom.RefreshButton;
import com.geekband.huzhouapp.utils.DataOperationHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 当前用户的问题列表Activity
 * 
 */
public class MyQuestionActivity extends FragmentActivity implements View.OnClickListener
{
	private ImageButton btn_back;
	private ListView lv_questionListView;
	private TextView tv_title;
	private ViewGroup vg_emptyTip;
	private ViewGroup vg_errorTip;
	private ViewGroup vg_progress;
	private RefreshButton btn_refresh;
	
	private QuestionListAdapter questionListAdapter;
	private int questionType; //根据问题类型加载不同的数据
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_myquestion);
		findView();
		initVar();
		initView();
		initListener();
		
		btn_refresh.beginRefresh();
	}
	
	private void findView()
	{
		btn_back = (ImageButton) findViewById(R.id.btn_myquestion_back);
		lv_questionListView = (ListView) findViewById(R.id.lv_myquestion_questionList);
		tv_title = (TextView) findViewById(R.id.tv_myquestion_title);
		vg_emptyTip = (ViewGroup) findViewById(R.id.vg_myquestion_emptyTip);
		vg_errorTip = (ViewGroup) findViewById(R.id.vg_myquestion_errorTip);
		vg_progress = (ViewGroup) findViewById(R.id.vg_myquestion_progress);
		btn_refresh = (RefreshButton) findViewById(R.id.vg_myquestion_refresh);
	}
	
	private void initView()
	{
		lv_questionListView.setAdapter(questionListAdapter);
		lv_questionListView.setVisibility(View.GONE);
		vg_emptyTip.setVisibility(View.GONE);
		vg_errorTip.setVisibility(View.GONE);
		switch(questionType)
		{
			case QUESTION_TYPE_MYASK: tv_title.setText("我的提问"); break;
			case QUESTION_TYPE_MYANSWER: tv_title.setText("我的回答"); break;
		}
	}
	
	private void initVar()
	{
		questionListAdapter = new QuestionListAdapter(this);
		
		if(getIntent().getExtras()!=null)
		{
			questionType = getIntent().getExtras().getInt(ARGS_QUESTIONTYPE);
		}
	}
	
	private void initListener()
	{
		btn_back.setOnClickListener(this);
		btn_refresh.setRefreshListener(new RefreshButton.RefreshListener()
		{
			@Override
			public void onRefresh()
			{
				runAsyncTask(AsyncDataLoader.TASK_INITLISTVIEW);
			}
		});
		
		lv_questionListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				int headerViewsCount = lv_questionListView.getHeaderViewsCount();
				Intent intent = new Intent();
				intent.setClass(MyQuestionActivity.this, QuestionDetailActivity.class);
				intent.putExtra(QuestionDetailActivity.ARGS_QUESTION, (Question) parent.getAdapter().getItem(position+headerViewsCount));
				startActivity(intent);
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
			case R.id.btn_myquestion_back:
			{
				finish();
			}break;
		}
	}
	
	private class QuestionListAdapter extends BaseAdapter
	{
		private Context context;
		
		private List<Question> questionList;
		
		public QuestionListAdapter(Context context)
		{
			this.context = context;
			questionList = new ArrayList<>();
		}
		
		public List<Question> getQuestionList()
		{
			return this.questionList;
		}
		
		@Override
		public int getCount()
		{
			return questionList.size();
		}

		@Override
		public Question getItem(int position)
		{
			return questionList.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHolder vh;
			if(convertView==null)
			{
				vh = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.item_myquestion_question, parent, false);
				vh.tv_title = (TextView) convertView.findViewById(R.id.tv_question_title);
				vh.tv_time = (TextView) convertView.findViewById(R.id.tv_question_time);
				vh.tv_answerNum = (TextView) convertView.findViewById(R.id.tv_question_answerNum);
				vh.tv_isReslove = (TextView) convertView.findViewById(R.id.tv_question_isResolved);
				convertView.setTag(vh);
			}
			else
			{
				vh = (ViewHolder) convertView.getTag();
			}
			
			vh.tv_title.setText(getItem(position).getAskInfo().getField(EnquiryTable.FIELD_CONTENT));
			vh.tv_time.setText(getItem(position).getAskInfo().getField(EnquiryTable.FIELD_APPLYTIME));
			vh.tv_answerNum.setText(""+getItem(position).getAnswerInfoList().size());
			vh.tv_isReslove.setText("");
			/*if(getItem(position).isResolve())
			{
				vh.tv_isReslove.setTextColor(0xFF02DF82);
				vh.tv_isReslove.setText("已解决");
			}
			else
			{
				vh.tv_isReslove.setTextColor(0xFFFF5809);
				vh.tv_isReslove.setText("未解决");
			}*/
			
			return convertView;
		}
		
		private class ViewHolder
		{
			private TextView tv_title;
			private TextView tv_time;
			private TextView tv_answerNum;
			private TextView tv_isReslove;
		}
	}

	private class AsyncDataLoader extends AsyncTask<Object, Integer, Integer>
	{
		private static final int TASK_INITLISTVIEW = 1;
		private static final int TASK_INITLISTVIEW_RESULT_SUCCESS = 1;
		private static final int TASK_INITLISTVIEW_RESULT_ERROR = -1;
		
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
				case TASK_INITLISTVIEW:
				{
					if(lv_questionListView.getVisibility()==View.GONE) vg_progress.setVisibility(View.VISIBLE);
					vg_emptyTip.setVisibility(View.GONE);
					vg_errorTip.setVisibility(View.GONE);
				}break;
			}
		}
		
		List<Question> questionList = new ArrayList<>();
		@Override
		protected Integer doInBackground(Object... params)
		{
			switch (task)
			{
				case TASK_INITLISTVIEW:
				{
					try
					{
						switch(questionType)
						{
							case QUESTION_TYPE_MYASK:
							{
								//当前用户(即提问者)
								UserTable asker = DataOperationHelper.queryCurrentUser();
								questionList = DataOperationHelper.queryUserAskQuestionList(asker);
							}break;
							
							case QUESTION_TYPE_MYANSWER:
							{
								//当前用户(即回答者)
								UserTable replier = DataOperationHelper.queryCurrentUser();
								questionList = DataOperationHelper.queryUserAnswerQuestionList(replier);
							}break;
						}
						
						return TASK_INITLISTVIEW_RESULT_SUCCESS;
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					
					return TASK_INITLISTVIEW_RESULT_ERROR;
				}
				
			}
			
			return 0;
		}
		
		@Override
		protected void onPostExecute(Integer taskResult)
		{
			switch (taskResult)
			{
				case TASK_INITLISTVIEW_RESULT_SUCCESS: //数据正常获取到了
				{
					if(questionList.size()!=0) //匹配到的数据不为空
					{
						questionListAdapter.getQuestionList().clear();
						questionListAdapter.getQuestionList().addAll(questionList);
						questionList.clear();
						questionListAdapter.notifyDataSetChanged();
						btn_refresh.refreshComplete();

						lv_questionListView.setVisibility(View.VISIBLE);
						vg_progress.setVisibility(View.GONE);
						vg_emptyTip.setVisibility(View.GONE);
						vg_errorTip.setVisibility(View.GONE);
					}
					else //匹配到的数据为空
					{
						btn_refresh.refreshComplete();
						
						lv_questionListView.setVisibility(View.GONE);
						vg_progress.setVisibility(View.GONE);
						vg_emptyTip.setVisibility(View.VISIBLE);
						vg_errorTip.setVisibility(View.GONE);
						//ToastUitl.showShort(MyQuestionActivity.this, "刷新完成");
					}
					
				}break;
				
				case TASK_INITLISTVIEW_RESULT_ERROR: //数据没有正常获取到
				{
					btn_refresh.refreshComplete();
					
					//当刷新数据出现异常时：
					//若当前列表中不存在数据，则直接用error tip占据屏幕
					//若当前列表中存在数据，则不用error tip占据屏幕，只是提示有异常
					//(防止情况：用户起初刷新到了数据，后面再刷新时出现了异常；此时如果用error tip占据屏幕就会顶掉原来的数据)
					if(questionListAdapter.getQuestionList().size()==0)
					{
						lv_questionListView.setVisibility(View.GONE);
						vg_errorTip.setVisibility(View.VISIBLE);
						//ToastUitl.showShort(MyQuestionActivity.this, "刷新完成");
					}
					else
					{
						lv_questionListView.setVisibility(View.VISIBLE);
						vg_errorTip.setVisibility(View.GONE);
					}
					vg_progress.setVisibility(View.GONE);
					vg_emptyTip.setVisibility(View.GONE);
				}break;
			}
		}
	}
	
	public static final String ARGS_QUESTIONTYPE = "QUESTIONTYPE";
	
	public static final int QUESTION_TYPE_MYASK = 0;
	public static final int QUESTION_TYPE_MYANSWER = 1;
}
