package com.geekband.huzhouapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.vo.CourseInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/27
 */
public class CourseListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<CourseInfo> mCourseList;

    public CourseListAdapter(Context context, ArrayList<CourseInfo> courseList) {
        mContext = context;
        mCourseList = courseList;
    }

    @Override
    public int getCount() {
        return mCourseList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCourseList.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_course_list,null);
            viewHolder.course_list_id = (TextView) convertView.findViewById(R.id.course_list_id);
            viewHolder.course_list_title = (TextView) convertView.findViewById(R.id.course_list_title);
            viewHolder.course_list_type = (TextView) convertView.findViewById(R.id.course_list_type);
            viewHolder.course_list_time = (TextView) convertView.findViewById(R.id.course_list_time);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.course_list_id .setText(String.valueOf(mCourseList.get(position).getId()));
        viewHolder.course_list_title .setText(mCourseList.get(position).getTitle());
        viewHolder.course_list_type .setText(mCourseList.get(position).getType());
        viewHolder.course_list_time .setText(mCourseList.get(position).getTime());

        return convertView;
    }

    class ViewHolder {
        private TextView course_list_id;
        private TextView course_list_title;
        private TextView course_list_type;
        private TextView course_list_time;
    }
}
