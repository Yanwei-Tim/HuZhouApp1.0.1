package com.chat.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chat.activity.OthersQuestionActivity;
import com.chat.activity.QuestionDetailActivity;
import com.chat.adapter.pojo.Answer;
import com.chat.adapter.pojo.Question;
import com.database.dto.DataOperation;
import com.database.pojo.EnquiryTable;
import com.database.pojo.ReplyTable;
import com.database.pojo.UserTable;
import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.custom.RefreshButton;
import com.geekband.huzhouapp.utils.BitmapHelper;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

public class OthersQuestionFragment extends Fragment
{
	private OthersQuestionActivity parentActivity;
	private View v_rootView;
	private ListView lv_questionListView;
	private ViewGroup vg_progress;
	private ViewGroup vg_errorTip;
	private RefreshButton btn_refresh;
	
	private OthersQuestionListAdapter othersQuestionListAdapter;
	private final String questionCategory;
	private boolean isInit;
	private Bundle fragmentStateValue;
	
	public OthersQuestionFragment(String questionCategory)
	{
		this.questionCategory = questionCategory;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		isInit = true;
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		
		parentActivity = (OthersQuestionActivity) activity;
		fragmentStateValue = parentActivity.getBundle();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		v_rootView = inflater.inflate(R.layout.fragment_othersquestion, container, false);
		
		findView();
		initVar();
		initView();
		initListener();

		if(isInit)
		{
			btn_refresh.beginRefresh();
			isInit = false;
		}
		else
		{
			if(fragmentStateValue.getSerializable(questionCategory)!=null)
			{
				lv_questionListView.setVisibility(View.VISIBLE);
				othersQuestionListAdapter.getQuestionList().addAll((ArrayList<Question>) fragmentStateValue.getSerializable(questionCategory));
				othersQuestionListAdapter.notifyDataSetChanged();
			}
		}
		
		return v_rootView;
	}
	
	private void findView()
	{
		lv_questionListView = (ListView) v_rootView.findViewById(R.id.lv_othersquestion_questionList);
		vg_progress = (ViewGroup) v_rootView.findViewById(R.id.vg_othersquestion_progress);
		vg_errorTip = (ViewGroup) v_rootView.findViewById(R.id.vg_othersquestion_errorTip);
		btn_refresh = (RefreshButton) v_rootView.findViewById(R.id.vg_othersquestion_refresh);
	}
	
	private void initView()
	{
		lv_questionListView.setAdapter(othersQuestionListAdapter);
		lv_questionListView.setVisibility(View.GONE);
		vg_progress.setVisibility(View.GONE);
		vg_errorTip.setVisibility(View.GONE);
	}
	
	private void initVar()
	{
		othersQuestionListAdapter = new OthersQuestionListAdapter();
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
		
		btn_refresh.setRefreshListener(new RefreshButton.RefreshListener()
		{
			@Override
			public void onRefresh()
			{
				runAsyncTask(AsyncDataLoader.TASK_INIT);
			}
		});
	}
	
	private void runAsyncTask(int task, Object... params)
	{
		new AsyncDataLoader(task, params).execute();
	}
	
	private class OthersQuestionListAdapter extends BaseAdapter
	{
		private List<Question> questionList;
		private BitmapUtils bitmapUtils;
		
		public OthersQuestionListAdapter()
		{
			questionList = new ArrayList<>();
			bitmapUtils = BitmapHelper.getBitmapUtils(getActivity(), lv_questionListView, R.drawable.head_default, R.drawable.head_default);
		}
		
		public List<Question> getQuestionList()
		{
			return questionList;
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
				convertView = LayoutInflater.from(OthersQuestionFragment.this.getActivity()).inflate(R.layout.item_othersquestion_question, parent, false);
				vh.iv_askerIcon = (ImageView) convertView.findViewById(R.id.iv_othersquestion_askerIcon);
				vh.tv_askerName = (TextView) convertView.findViewById(R.id.tv_othersquestion_askerName);
				vh.tv_askContent = (TextView) convertView.findViewById(R.id.tv_othersquestion_askContent);
				vh.tv_replierName = (TextView) convertView.findViewById(R.id.tv_othersquestion_replierName);
				vh.tv_answerContent = (TextView) convertView.findViewById(R.id.tv_othersquestion_answerContent);
				vh.tv_replierNum = (TextView) convertView.findViewById(R.id.tv_othersquestion_replierNum);
				vh.vg_answer = (ViewGroup) convertView.findViewById(R.id.vg_othersquestion_answer);
				convertView.setTag(vh);
			}
			else
			{
				vh = (ViewHolder) convertView.getTag();
			}
			
			String headIconUrl = "";
			if(getItem(position).getAskerInfo().getAccessaryFileUrlList()!=null && getItem(position).getAskerInfo().getAccessaryFileUrlList().size()!=0 )
			{
				headIconUrl = getItem(position).getAskerInfo().getAccessaryFileUrlList().get(0);
			}
			bitmapUtils.display(vh.iv_askerIcon, headIconUrl);
			
			vh.tv_askerName.setText(getItem(position).getAskerInfo().getField(UserTable.FIELD_REALNAME));
			vh.tv_askContent.setText(getItem(position).getAskInfo().getField(EnquiryTable.FIELD_CONTENT));
			vh.tv_replierNum.setText(""+getItem(position).getAnswerInfoList().size());
			if(getItem(position).getAnswerInfoList().size()!=0)
			{
				vh.vg_answer.setVisibility(View.VISIBLE);
				vh.tv_replierName.setText(getItem(position).getAnswerInfoList().get(0).getReplierInfo().getField(UserTable.FIELD_REALNAME));
				vh.tv_answerContent.setText(getItem(position).getAnswerInfoList().get(0).getAnswerInfo().getField(ReplyTable.FIELD_CONTENT));
			}
			else
			{
				vh.vg_answer.setVisibility(View.GONE);
			}
			
			return convertView;
		}
		
		private class ViewHolder
		{
			private ImageView iv_askerIcon;
			private TextView tv_askerName;
			private TextView tv_askContent;
			private TextView tv_replierNum;
			private TextView tv_replierName;
			private TextView tv_answerContent;
			private ViewGroup vg_answer;
		}
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
					if(lv_questionListView.getVisibility()==View.GONE) vg_progress.setVisibility(View.VISIBLE);
				}break;
			}
		}
		
		ArrayList<Question> questionList = new ArrayList<>();
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
								
								questionList.add(new Question((UserTable) DataOperation.queryTable(UserTable.TABLE_NAME, UserTable.CONTENTID, ask.getField(EnquiryTable.FIELD_USERNO)).get(0), ask, answerList));
							}
						}

						return TASK_INIT_RESULT_SUCCESS;
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					
					return TASK_INIT_RESULT_ERROR;
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
					othersQuestionListAdapter.getQuestionList().clear();
					othersQuestionListAdapter.getQuestionList().addAll(questionList);
					othersQuestionListAdapter.notifyDataSetChanged();
					questionList.clear();
					btn_refresh.refreshComplete();
					parentActivity.getBundle().putSerializable(questionCategory, (ArrayList<Question>)othersQuestionListAdapter.getQuestionList());
					
					lv_questionListView.setVisibility(View.VISIBLE);
					vg_progress.setVisibility(View.GONE);
					vg_errorTip.setVisibility(View.GONE);
				}break;
				
				case TASK_INIT_RESULT_ERROR:
				{
					btn_refresh.refreshComplete();
					
					if(othersQuestionListAdapter.getQuestionList().size()==0)
					{
						lv_questionListView.setVisibility(View.GONE);
						vg_errorTip.setVisibility(View.VISIBLE);
					}
					else
					{
						lv_questionListView.setVisibility(View.VISIBLE);
						vg_errorTip.setVisibility(View.GONE);
					}
					vg_progress.setVisibility(View.GONE);
				}break;
			}
		}
	}
}
