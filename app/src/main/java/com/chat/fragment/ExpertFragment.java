package com.chat.fragment;

import android.annotation.SuppressLint;
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

import com.chat.adapter.ExpertListAdapter;
import com.chat.adapter.pojo.Expert;
import com.database.dto.DataOperation;
import com.database.pojo.ExpertTable;
import com.database.pojo.UserInfoTable;
import com.database.pojo.UserTable;
import com.geekband.huzhouapp.R;

import java.util.List;

public class ExpertFragment extends Fragment
{
	private View v_rootView;
	private ListView lv_expertListView;
	private ViewGroup vg_progress;
	
	private ExpertListAdapter expertListAdapter;
	private String expertCategory;



	@SuppressLint("ValidFragment")
	public ExpertFragment(String expertCategory)
	{
		this.expertCategory = expertCategory;
	}

	public ExpertFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		v_rootView = inflater.inflate(R.layout.fragment_expert, container, false);
		
		return v_rootView;
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		
		findView();
		initVar();
		initView();
		initListener();
		runAsyncTask(AsyncDataLoader.TASK_INITLISTVIEW); //更新listView
	}
	
	private void findView()
	{
		lv_expertListView = (ListView) v_rootView.findViewById(R.id.lv_expert_expertlist);
		vg_progress = (ViewGroup) v_rootView.findViewById(R.id.vg_expert_progress);
	}
	
	private void initView()
	{
		lv_expertListView.setAdapter(expertListAdapter);
	}
	
	private void initVar()
	{
		expertListAdapter = new ExpertListAdapter(getActivity(), lv_expertListView);
	}
	
	private void initListener()
	{
		lv_expertListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Toast.makeText(getActivity(), "查看详情", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void runAsyncTask(int task, Object... params)
	{
		new AsyncDataLoader(task, params).execute();
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
					vg_progress.setVisibility(View.VISIBLE);
					lv_expertListView.setVisibility(View.GONE);
				}break;
			}
		}

		@Override
		protected Integer doInBackground(Object... params)
		{
			switch (task)
			{
				case TASK_INITLISTVIEW:
				{
					try
					{
						//专家列表
						List<ExpertTable> expertList = (List<ExpertTable>) DataOperation.queryTable(ExpertTable.TABLE_NAME);
						if(expertList!=null)
						{
							for (ExpertTable expertTable : expertList)
							{
								//专家的个人相关信息
								UserTable userData = null;
								UserInfoTable userInfoData = null;
								
								List<UserTable> uList = (List<UserTable>) DataOperation.queryTable(UserTable.TABLE_NAME, UserTable.CONTENTID, expertTable.getField(ExpertTable.FIELD_USERNO));
								if(uList!=null && uList.size()!=0) userData = uList.get(0);
								List<UserInfoTable> uIList = null;
								if(userData!=null) uIList= (List<UserInfoTable>) DataOperation.queryTable(UserInfoTable.TABLE_NAME, UserInfoTable.FIELD_USERID, userData.getContentId());
								if(uIList!=null && uIList.size()!=0) userInfoData = (UserInfoTable) DataOperation.queryTable(UserInfoTable.TABLE_NAME, UserInfoTable.FIELD_USERID, userData.getContentId()).get(0);
								
								//添加item(添加专家)到listView
								expertListAdapter.getExpertList().add(new Expert(expertTable, userData, userInfoData));
							}
						}
						
						return TASK_INITLISTVIEW_RESULT_SUCCESS;
					}
					catch (Exception e)
					{
						e.printStackTrace();
						return TASK_INITLISTVIEW_RESULT_ERROR;
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
				case TASK_INITLISTVIEW_RESULT_SUCCESS:
				{
					vg_progress.setVisibility(View.GONE);
					lv_expertListView.setVisibility(View.VISIBLE);
					expertListAdapter.notifyDataSetChanged();
				}break;
				
				case TASK_INITLISTVIEW_RESULT_ERROR:
				{
					vg_progress.setVisibility(View.GONE);
					lv_expertListView.setVisibility(View.VISIBLE);
					Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
				}break;
			}
		}
	}
}
