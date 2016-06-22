package com.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.chat.adapter.ExpertPagerAdapter;
import com.geekband.huzhouapp.R;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

public class ExpertActivity extends FragmentActivity implements View.OnClickListener
{
	private ImageButton btn_back;
	private TabPageIndicator tpi_categories;
	private ViewPager vp_pager;
	
	private ExpertPagerAdapter expertPagerAdapter;
	private List<String> expertCategoryList;
	
	@Override
	protected void onCreate(Bundle saveInstanceState)
	{
		super.onCreate(saveInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_expert);
		findView();
		initVar();
		initView();
		initListener();

		Intent intent = getIntent();
		int position = intent.getIntExtra("class",0);
		vp_pager.setCurrentItem(position+1);

	}
	
	private void findView()
	{
		btn_back = (ImageButton) findViewById(R.id.btn_expert_back);
		tpi_categories = (TabPageIndicator) findViewById(R.id.tpi_expert_categories);
		vp_pager = (ViewPager) findViewById(R.id.vp_expert_pager);
	}
	
	private void initView()
	{
		vp_pager.setAdapter(expertPagerAdapter);
		tpi_categories.setViewPager(vp_pager);
	}
	
	private void initVar()
	{
		expertCategoryList = new ArrayList<>();
		expertCategoryList.add("全部");
		expertCategoryList.add("法律");
		expertCategoryList.add("刑侦");
		expertCategoryList.add("交警");
		expertCategoryList.add("消防");
		expertCategoryList.add("心理");
		expertCategoryList.add("政工");
		expertCategoryList.add("公文写作");
		expertCategoryList.add("信息化");
		expertPagerAdapter = new ExpertPagerAdapter(getSupportFragmentManager(), expertCategoryList);
	}
	
	private void initListener()
	{
		btn_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.btn_expert_back:
			{
				finish();
			}break;
		}
	}
}
