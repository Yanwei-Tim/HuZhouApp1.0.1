package com.geekband.huzhouapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.vo.CourseClass;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/16
 * 专家咨询栏目分类
 */
public class CourseClassAdapter extends BaseAdapter {

    ArrayList<CourseClass> courseList ;
    Context mContext;

    public CourseClassAdapter(ArrayList<CourseClass> list, Context context) {
        this.courseList = list;
        mContext = context;
    }

    @Override
    public int getCount() {
        return courseList.size();
    }

    @Override
    public Object getItem(int position) {
        return courseList.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_course,null);
            viewHolder.course_ico_item = (ImageView) convertView.findViewById(R.id.course_ico_item);
            viewHolder.course_normal_item = (ImageView) convertView.findViewById(R.id.course_normal_item);
            viewHolder.course_title_item = (TextView) convertView.findViewById(R.id.course_title_item);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.course_ico_item.setImageResource(courseList.get(position).getImageId());
        viewHolder.course_normal_item.setImageResource(R.drawable.app_ui_ai_icon_next_c);
        viewHolder.course_title_item.setText(courseList.get(position).getTitle());

        return convertView;
    }

    class ViewHolder {
        private ImageView course_ico_item;
        private ImageView course_normal_item;
        private TextView course_title_item;
    }
}
