package com.geekband.huzhouapp.fragment.news;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.adapter.CourseListAdapter;
import com.geekband.huzhouapp.application.MyApplication;
import com.geekband.huzhouapp.utils.BaseInfo;
import com.geekband.huzhouapp.utils.Constants;
import com.geekband.huzhouapp.vo.CourseInfo;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/29
 */
public class CourseListActivity extends Activity implements AdapterView.OnItemClickListener ,View.OnClickListener{

    private ProgressBar mCourse_progress;
    private TextView mNo_course_text;
    private ListView mCourse_listView;
    private ArrayList<CourseInfo> mCourseList;
    private Button mCourseList_backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        mCourse_listView = (ListView) findViewById(R.id.course_listView);
        mCourse_progress = (ProgressBar) findViewById(R.id.course_progress);
        mNo_course_text = (TextView) findViewById(R.id.no_course_text);
        mCourseList_backBtn = (Button) findViewById(R.id.courseList_backBtn);

        mCourse_listView.setOnItemClickListener(this);
        mCourseList_backBtn.setOnClickListener(this);

        //加载网络数据
        new NetTask().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, CourseContentActivity.class);
        if (mCourseList != null) {
            ArrayList<CharSequence> cs = new ArrayList<>();
            String title = mCourseList.get(position).getTitle();
            String type = mCourseList.get(position).getType();
            String time = mCourseList.get(position).getTime();
            String intro = mCourseList.get(position).getIntro();
            String detailed = mCourseList.get(position).getDetailed();
            cs.add(title);
            cs.add(type);
            cs.add(time);
            cs.add(intro);
            cs.add(detailed);
            intent.putCharSequenceArrayListExtra(Constants.COURSE_CONTENT, cs);
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.courseList_backBtn:
//                startActivity(new Intent(this, MainActivity.class));
                this.finish();
                break;
        }
    }


    private class NetTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            new LocalTask().execute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            String contentId = MyApplication.sSharedPreferences.getString(Constants.AUTO_LOGIN,null);
            BaseInfo.saveCourse(contentId);
            return null;
        }
    }

    private class LocalTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            mCourse_progress.setVisibility(View.VISIBLE);
            mCourse_listView.setVisibility(View.GONE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                mCourseList = (ArrayList<CourseInfo>) MyApplication.sDbUtils.findAll(CourseInfo.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            mCourse_progress.setVisibility(View.GONE);
            if (mCourseList != null) {
                mNo_course_text.setVisibility(View.GONE);
                mCourse_listView.setVisibility(View.VISIBLE);
                initCourseList();
            } else {
                mNo_course_text.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initCourseList() {
            CourseListAdapter courseListAdapter = new CourseListAdapter(this, mCourseList);
            mCourse_listView.setAdapter(courseListAdapter);
    }
}
