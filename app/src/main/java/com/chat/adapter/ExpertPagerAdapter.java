package com.chat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.chat.fragment.ExpertFragment;

import java.util.ArrayList;
import java.util.List;

public class ExpertPagerAdapter extends FragmentPagerAdapter
{
	private List<String> expertCategoryList = new ArrayList<>();
	private List<Fragment> fragmentList = new ArrayList<>();
	
	public ExpertPagerAdapter(FragmentManager fm, List<String> expertCategoryList)
	{
		super(fm);
		
		if(expertCategoryList!=null)
		{
			this.expertCategoryList.addAll(expertCategoryList);
			
			for (String string : expertCategoryList)
			{
				fragmentList.add(new ExpertFragment(string));
			}
		}
	}
	
	@Override
	public int getCount()
	{
		return expertCategoryList.size();
	}
	
	@Override
	public Fragment getItem(int position)
	{
		return fragmentList.get(position);
	}
	
	@Override
	public CharSequence getPageTitle(int position)
	{
		return expertCategoryList.get(position);
	}
}
