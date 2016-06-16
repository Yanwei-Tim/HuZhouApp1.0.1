package com.chat.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.chat.adapter.pojo.Question;
import com.database.dto.DataOperation;
import com.database.pojo.EnquiryTable;
import com.database.pojo.UserTable;
import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.application.MyApplication;
import com.geekband.huzhouapp.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateQuestionActivity extends BaseActivity implements View.OnClickListener
{
	private ImageButton btn_back;
	private EditText et_content;
	private Button btn_submit;
	private Button btn_category;
	
	private int selectedCategory;
	private String[] categories = new String[]{"法律", "刑侦", "交警", "消防", "心理", "政工", "公文写作", "信息化"};
	
	@Override
	protected void onCreate(Bundle saveInstanceState)
	{
		super.onCreate(saveInstanceState);
		
		setContentView(R.layout.activity_createquestion);
		findView();
		initVar();
		initView();
		initListener();
	}
	
	private void findView()
	{
		btn_back = (ImageButton) findViewById(R.id.btn_createquestion_back);
		et_content = (EditText) findViewById(R.id.et_createquestion_content);
		btn_submit = (Button) findViewById(R.id.btn_createquestion_submin);
		btn_category = (Button) findViewById(R.id.btn_createquestion_category);
	}
	
	private void initView()
	{
		
	}
	
	private void initVar()
	{
		selectedCategory = 0;
	}
	
	private void initListener()
	{
		btn_back.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		btn_category.setOnClickListener(this);
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
			case R.id.btn_createquestion_back:
			{
				finish();
			}break;

			case R.id.btn_createquestion_submin:
			{
				String content = et_content.getText().toString();
				if(content!=null && !"".equals(content))
				{
					/*//Question question = new Question(R.drawable.head10, "小不点儿", content, "50秒前", false, null);
					Intent intent = new Intent();
					intent.setClass(this, QuestionDetailActivity.class);
					//intent.putExtra(QuestionDetailActivity.ARGS_QUESTION, question);
					startActivity(intent);
					finish();*/
					runAsyncTask(AsyncDataLoader.TASK_INIT, content);
				}
				else
				{
					Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
				}
			}break;

			case R.id.btn_createquestion_category:
			{
				AlertDialog.Builder dialog = new AlertDialog.Builder(this);
				dialog.setCancelable(true);
				dialog.setTitle("选择分类");
				dialog.setSingleChoiceItems(categories, selectedCategory, new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						selectedCategory = which;
					}
				});
				dialog.setPositiveButton("确定", new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
				dialog.show();
			}break;
		}
	}
	
	private class AsyncDataLoader extends AsyncTask<Object, Integer, Integer>
	{
		private static final int TASK_INIT = 1;
		private static final int TASK_INIT_RESULT_SUCCESS = 1;
		private static final int TASK_INIT_RESULT_ERROR = -1;
		
		private int task;
		private Object[] params;
		
		public AsyncDataLoader(int task, Object...params)
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
						//问题的内容
						String content = (String) this.params[0];
						
						//当前用户
						UserTable currentUser = (UserTable) DataOperation.queryTable(UserTable.TABLE_NAME, UserTable.CONTENTID, MyApplication.sSharedPreferences.getString(Constants.AUTO_LOGIN, "")).get(0);
						
						//创建EnquiryTable(代表一个问题)
						EnquiryTable enquiryData = new EnquiryTable();
						enquiryData.putField(EnquiryTable.FIELD_USERNO, currentUser.getContentId());
						enquiryData.putField(EnquiryTable.FIELD_CONTENT, content);
						enquiryData.putField(EnquiryTable.FIELD_APPLYTIME, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(System.currentTimeMillis())));
						enquiryData.putField(EnquiryTable.FIELD_CATEGORYID, "");
						
						//将新建的EnquiryTable数据上传到服务器
						boolean result = DataOperation.insertOrUpdateTable(enquiryData, null);
						if(!result) return TASK_INIT_RESULT_ERROR; //若上传失败，则返回
						
						//问题创建成功后，关闭当前Activity，并跳转到新创建的问题的详情页面
						Question question = new Question(currentUser, enquiryData, null);
						Intent intent = new Intent();
						intent.setClass(CreateQuestionActivity.this, QuestionDetailActivity.class);
						intent.putExtra(QuestionDetailActivity.ARGS_QUESTION, question);
						startActivity(intent);
						finish();
						
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
					
				}break;
				
				case TASK_INIT_RESULT_ERROR:
				{
					
				}break;
			}
		}
	}
}
