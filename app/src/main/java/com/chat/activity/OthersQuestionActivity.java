package com.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.chat.adapter.OthersQuestionPagerAdapter;
import com.geekband.huzhouapp.R;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

public class OthersQuestionActivity extends BaseActivity implements View.OnClickListener
{
	private ImageButton btn_back;
	private TabPageIndicator tpi_categories;
	private ViewPager vp_pager;
	private Button btn_createQuestion;
	
	private OthersQuestionPagerAdapter othersQuestionPagerAdapter;
	private List<String> titleList = new ArrayList<>();
	
	@Override
	protected void onCreate(Bundle saveInstanceState)
	{
		super.onCreate(saveInstanceState);
		
		setContentView(R.layout.activity_othersquestion);
		findView();
		initVar();
		initView();
		initListener();
	}
	
	private void findView()
	{
		btn_back = (ImageButton) findViewById(R.id.btn_othersquestion_back);
		tpi_categories = (TabPageIndicator) findViewById(R.id.tpi_othersquestion_categories);
		vp_pager = (ViewPager) findViewById(R.id.vp_othersquestion_pager);
		btn_createQuestion = (Button) findViewById(R.id.btn_othersquestion_createQuestion);
	}

	private void initView()
	{
		vp_pager.setAdapter(othersQuestionPagerAdapter);
		tpi_categories.setViewPager(vp_pager);
	}

	private void initVar()
	{
		titleList.add("全部");
		titleList.add("法律");
		titleList.add("刑侦");
		titleList.add("交警");
		titleList.add("消防");
		titleList.add("心理");
		titleList.add("政工");
		titleList.add("公文写作");
		titleList.add("信息化");
		othersQuestionPagerAdapter = new OthersQuestionPagerAdapter(getSupportFragmentManager(), titleList);
	}

	private void initListener()
	{
		btn_back.setOnClickListener(this);
		btn_createQuestion.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.btn_othersquestion_back:
			{
				finish();
			}break;
			
			case R.id.btn_othersquestion_createQuestion: //提问
			{
				Intent intent = new Intent(this, CreateQuestionActivity.class);
				startActivity(intent);
			}break;
		}
	}
}
