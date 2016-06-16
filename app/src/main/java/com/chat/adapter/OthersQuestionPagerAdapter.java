package com.chat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.chat.fragment.OthersQuestionFragment;

import java.util.ArrayList;
import java.util.List;

public class OthersQuestionPagerAdapter extends FragmentPagerAdapter
{
	private List<String> titleList = new ArrayList<>();
	
	public OthersQuestionPagerAdapter(FragmentManager fm, List<String> titleList)
	{
		super(fm);
		
		if(titleList!=null) this.titleList.addAll(titleList);
	}

	@Override
	public int getCount()
	{
		return titleList.size();
	}
	
	@Override
	public Fragment getItem(int arg0)
	{
		return new OthersQuestionFragment();
	}
	
	@Override
	public CharSequence getPageTitle(int position)
	{
		return titleList.get(position);
	}
}
