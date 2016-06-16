package com.chat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chat.activity.ExpertActivity;
import com.chat.activity.OthersQuestionActivity;
import com.chat.activity.PersonalQuestionActivity;
import com.chat.adapter.QuestionMenuItemListAdapter;
import com.chat.adapter.pojo.QuestionMenuItem;
import com.geekband.huzhouapp.R;

public class QuestionMenuFragment extends Fragment
{
	private View v_rootView;
	private ListView lv_questionMenuListView;
	
	private QuestionMenuItemListAdapter questionMenuItemListAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		v_rootView = inflater.inflate(R.layout.fragment_questionmenu, container, false);
		
		findView();
		initVar();
		initView();
		initListener();
		
		return v_rootView;
	}
	
	private void findView()
	{
		lv_questionMenuListView = (ListView) v_rootView.findViewById(R.id.lv_questionmenu_menuItemList);
	}
	
	private void initView()
	{
		lv_questionMenuListView.setAdapter(questionMenuItemListAdapter);
	}
	
	private void initVar()
	{
		questionMenuItemListAdapter = new QuestionMenuItemListAdapter(getActivity());
		
		questionMenuItemListAdapter.addItem(new QuestionMenuItem(R.drawable.app_icon_org_contacts, MENU_ITEM_1));
		questionMenuItemListAdapter.addItem(new QuestionMenuItem(R.drawable.app_icon_wddb, MENU_ITEM_2));
		questionMenuItemListAdapter.addItem(new QuestionMenuItem(R.drawable.app_icon_t, MENU_ITEM_3));
	}
	
	private void initListener()
	{
		lv_questionMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				switch(position)
				{
					case 0:
					{
						Intent intent = new Intent();
						intent.setClass(getActivity(), PersonalQuestionActivity.class);
						startActivity(intent);
					}break;

					case 1:
					{
						Intent intent = new Intent();
						intent.setClass(getActivity(), ExpertActivity.class);
						startActivity(intent);
					}break;

					case 2:
					{
						Intent intent = new Intent();
						intent.setClass(getActivity(), OthersQuestionActivity.class);
						startActivity(intent);
					}break;
				}
			}
		});
	}
	
	public static final String MENU_ITEM_1 = "我的提问";
	public static final String MENU_ITEM_2 = "咨询专家";
	public static final String MENU_ITEM_3 = "最新问答";
}
