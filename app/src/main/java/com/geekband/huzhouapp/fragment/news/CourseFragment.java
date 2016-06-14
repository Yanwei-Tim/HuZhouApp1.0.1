package com.geekband.huzhouapp.fragment.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.adapter.CourseClassAdapter;
import com.geekband.huzhouapp.vo.CourseClass;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/12
 * 日志模块
 */
public class CourseFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static CourseFragment newInstance() {
        return new CourseFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, null);
        ListView courseClass_listView = (ListView) view.findViewById(R.id.courseClass_listView);

        courseClass_listView.setOnItemClickListener(this);
        //初始化
        initCourseClass(courseClass_listView);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void initCourseClass(ListView course_listView) {
        ArrayList<CourseClass> courseList = new ArrayList<>();
        courseList.add(new CourseClass(R.drawable.app_icon_org_contacts, "全部课程"));
        courseList.add(new CourseClass(R.drawable.app_icon_wddb, "已选课程"));
        courseList.add(new CourseClass(R.drawable.app_icon_t, "成绩表单"));
        CourseClassAdapter courseClassAdapter = new CourseClassAdapter(courseList, getActivity());
        course_listView.setAdapter(courseClassAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position==0){
            startActivity(new Intent(getActivity(), CourseListActivity.class));
        }else if (position==2){
            startActivity(new Intent(getActivity(),GradeActivity.class));
        }
    }

}



