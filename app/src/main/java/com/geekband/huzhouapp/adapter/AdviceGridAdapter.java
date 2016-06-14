package com.geekband.huzhouapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.vo.ClassInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/16
 * 专家咨询栏目分类
 */
public class AdviceGridAdapter extends BaseAdapter {

    ArrayList<ClassInfo> classList ;
    Context mContext;

    public AdviceGridAdapter(ArrayList<ClassInfo> classList, Context context) {
        this.classList = classList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return classList.size();
    }

    @Override
    public Object getItem(int position) {
        return classList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_class,null);
            viewHolder.class_imageView_item = (ImageView) convertView.findViewById(R.id.class_imageView_item);
            viewHolder.class_title_item = (TextView) convertView.findViewById(R.id.class_title_item);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.class_imageView_item.setImageResource(classList.get(position).getImageId());
        viewHolder.class_title_item.setText(classList.get(position).getClassTitle());

        return convertView;
    }

    class ViewHolder {
        private ImageView class_imageView_item;
        private TextView class_title_item;
    }
}
