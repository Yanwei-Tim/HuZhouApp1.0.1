package com.geekband.huzhouapp.baseadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/14
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<T> mArrayList;
    private int mItemLayoutId ;


    public CommonAdapter(Context context, ArrayList<T> arrayList,int itemLayoutId) {

        mContext = context;
        mArrayList = arrayList;
        mLayoutInflater = LayoutInflater.from(context);
        mItemLayoutId = itemLayoutId;
    }


    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = getViewHolder(position, convertView, parent);
        //noinspection unchecked
        convert(viewHolder, (T) getItem(position));
        return viewHolder.getConvertView();
    }

    public abstract void convert (ViewHolder viewHolder,T item);

    private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent){
        return ViewHolder.getViewHolder(mContext, convertView, parent, mItemLayoutId, position);
    }
}
