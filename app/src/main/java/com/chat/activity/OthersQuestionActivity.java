package com.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.chat.fragment.OthersQuestionFragment;
import com.geekband.huzhouapp.R;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

public class OthersQuestionActivity extends FragmentActivity implements View.OnClickListener
{
	private ImageButton btn_back;
	private TabPageIndicator tpi_categories;
	private ViewPager vp_pager;
	private Button btn_createQuestion;
	
	private OthersQuestionPagerAdapter othersQuestionPagerAdapter;
	private List<String> questionCategoryList = new ArrayList<>();
	private Bundle args;
	
	@Override
	protected void onCreate(Bundle saveInstanceState)
	{
		super.onCreate(saveInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
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
		questionCategoryList.add("全部");
		questionCategoryList.add("法律");
		questionCategoryList.add("刑侦");
		questionCategoryList.add("交警");
		questionCategoryList.add("消防");
		questionCategoryList.add("心理");
		questionCategoryList.add("政工");
		questionCategoryList.add("公文写作");
		questionCategoryList.add("信息化");
		othersQuestionPagerAdapter = new OthersQuestionPagerAdapter(getSupportFragmentManager());
		
		args = new Bundle();
	}

	private void initListener()
	{
		btn_back.setOnClickListener(this);
		btn_createQuestion.setOnClickListener(this);
	}
	
	public Bundle getBundle()
	{
		return args;
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
	
	public class OthersQuestionPagerAdapter extends FragmentPagerAdapter
	{
		
		public OthersQuestionPagerAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public int getCount()
		{
			return questionCategoryList.size();
		}
		
		@Override
		public Fragment getItem(int position)
		{
			return new OthersQuestionFragment(questionCategoryList.get(position));
		}
		
		@Override
		public CharSequence getPageTitle(int position)
		{
			return questionCategoryList.get(position);
		}
	}
}
