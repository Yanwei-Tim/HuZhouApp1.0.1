package com.geekband.huzhouapp.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.geekband.huzhouapp.R;


public class PagingLoadListView extends ListView implements OnScrollListener
{
	private View footer;
	private int totalItemCount;
	private int lastVisibleItem;
	private boolean isLoading;

	public PagingLoadListView(Context context)
	{
		super(context);
		initView(context);
	}

	public PagingLoadListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(context);
	}

	public PagingLoadListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		footer = inflater.inflate(R.layout.layout_listviewfooter, null);
		footer.findViewById(R.id.vg_footer).setVisibility(View.GONE);
		this.addFooterView(footer);
		this.setOnScrollListener(this);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		this.lastVisibleItem = firstVisibleItem + visibleItemCount;
		this.totalItemCount = totalItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		if (totalItemCount == lastVisibleItem && scrollState == SCROLL_STATE_IDLE)
		{
			if (!isLoading)
			{
				isLoading = true;
				footer.findViewById(R.id.vg_footer).setVisibility(View.VISIBLE);
				if(onLoadListener!=null) onLoadListener.onLoad();
			}
		}
	}

	public void loadComplete()
	{
		isLoading = false;
		footer.findViewById(R.id.vg_footer).setVisibility(View.GONE);
	}

	private OnLoadListener onLoadListener;
	public void setOnLoadListener(OnLoadListener onLoadListener)
	{
		this.onLoadListener = onLoadListener;
	}
	public interface OnLoadListener
	{
		void onLoad();
	}
}
