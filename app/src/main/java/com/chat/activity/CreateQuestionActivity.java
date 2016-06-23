package com.chat.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chat.adapter.pojo.Question;
import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.utils.DataOperationHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateQuestionActivity extends Activity implements View.OnClickListener
{
	private ImageButton btn_back;
	private TextView tv_title;
	private EditText et_content;
	private Button btn_submit;
	
	private int selectedCategory;
	private String[] categories = new String[]{"法律", "刑侦", "交警", "消防", "心理", "政工", "公文写作", "信息化"};
	private Spinner mSpinner;

	@Override
	protected void onCreate(Bundle saveInstanceState)
	{
		super.onCreate(saveInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_createquestion);
		findView();
		initVar();
		initView();
		initListener();
	}
	
	private void findView()
	{
		btn_back = (ImageButton) findViewById(R.id.btn_createquestion_back);
		tv_title = (TextView) findViewById(R.id.tv_createquestion_title);
		et_content = (EditText) findViewById(R.id.et_createQuestion_content);
		btn_submit = (Button) findViewById(R.id.btn_createquestion_submin);
		mSpinner = (Spinner) findViewById(R.id.spinner_question_type);
		mSpinner.setBackgroundResource(R.drawable.abc_spinner_ab_pressed_holo_light);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,categories);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(arrayAdapter);
	}
	
	private void initView()
	{
		tv_title.setText("发布新问题");
	}
	
	private void initVar()
	{
		selectedCategory = 0;
	}
	
	private void initListener()
	{
		btn_back.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
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
				//问题内容
				String content = et_content.getText().toString();
				//问题类型
				String type = mSpinner.getSelectedItem().toString();

				if(content!=null && !"".equals(content))
				{
					runAsyncTask(AsyncDataLoader.TASK_CREATEQUESTION, content,type);
				}
				else
				{
					Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
				}
			}break;

		}
	}
	
	private class AsyncDataLoader extends AsyncTask<Object, Integer, Integer>
	{
		private static final int TASK_CREATEQUESTION = 1;
		private static final int TASK_CREATEQUESTION_RESULT_SUCCESS = 1;
		private static final int TASK_CREATEQUESTION_RESULT_ERROR = -1;
		
		private int task;
		private Object[] params;
		private ProgressDialog mPd;

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
				case TASK_CREATEQUESTION: //显示问题发布进度条对话框，并执行问题发布任务
				{
					mPd = ProgressDialog.show(CreateQuestionActivity.this, null, "正在上上传...");
					
				}break;
			}
		}
		
		Question question;
		@Override
		protected Integer doInBackground(Object... params)
		{
			switch (task)
			{
				case TASK_CREATEQUESTION:
				{
					try
					{
						//问题的内容
						String content = (String) this.params[0];
						//问题类型
						String type = (String) this.params[1];
						
						question = DataOperationHelper.uploadQuestion(
								DataOperationHelper.queryCurrentUser(),
								content,
								new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(System.currentTimeMillis())),
								type);
						
						if(question!=null) return TASK_CREATEQUESTION_RESULT_SUCCESS;
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					
					return TASK_CREATEQUESTION_RESULT_ERROR;
				}
			}
			
			return 0;
		}
		
		@Override
		protected void onPostExecute(Integer taskResult)
		{
			switch (taskResult)
			{
				case TASK_CREATEQUESTION_RESULT_SUCCESS:
				{
					mPd.setMessage("上传成功！");
					mPd.dismiss();
					et_content.setText(null);
					//问题创建成功后，关闭当前Activity，并跳转到新创建的问题的详情页面
					Intent intent = new Intent();
					intent.setClass(CreateQuestionActivity.this, QuestionDetailActivity.class);
					intent.putExtra(QuestionDetailActivity.ARGS_QUESTION, question);
					startActivity(intent);
					finish();
				}break;
				
				case TASK_CREATEQUESTION_RESULT_ERROR:
				{
					mPd.setMessage("上传失败！");
					mPd.dismiss();
				}break;
			}
		}
	}
}
