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
import com.geekband.huzhouapp.application.MyApplication;
import com.geekband.huzhouapp.baseadapter.CommonAdapter;
import com.geekband.huzhouapp.baseadapter.ViewHolder;
import com.geekband.huzhouapp.utils.DataUtils;
import com.geekband.huzhouapp.utils.Constants;
import com.geekband.huzhouapp.vo.CourseInfo;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/29
 */
public class CourseListActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

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


        mCourseList_backBtn.setOnClickListener(this);

        new LocalTask().execute();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //加载网络数据
        new NetTask().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CourseInfo courseInfo = mCourseList.get(position);
        Intent intent = new Intent(this, CourseContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.COURSE_CONTENT, courseInfo);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.courseList_backBtn:
//                startActivity(new Intent(this, MainActivity.class));
                this.finish();
                break;
        }
    }


    private class NetTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            String contentId = MyApplication.sSharedPreferences.getString(Constants.AUTO_LOGIN, null);
            DataUtils.saveCourse(contentId);
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
        mCourse_listView.setAdapter(new CommonAdapter<CourseInfo>(this,mCourseList,R.layout.item_course_list) {
            @Override
            public void convert(ViewHolder viewHolder, CourseInfo item) {
                viewHolder.setText(R.id.course_list_id,String.valueOf(item.getId()));
                viewHolder.setText(R.id.course_list_title,item.getTitle());
                viewHolder.setText(R.id.course_list_type,item.getType());
                viewHolder.setText(R.id.course_list_time,item.getTime());
            }
        });
        mCourse_listView.setOnItemClickListener(this);
    }
}
