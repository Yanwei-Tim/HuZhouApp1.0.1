package com.geekband.huzhouapp.fragment.news;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.application.MyApplication;
import com.geekband.huzhouapp.utils.BaseInfo;
import com.geekband.huzhouapp.utils.Constants;
import com.geekband.huzhouapp.vo.GradeInfo;
import com.lidroid.xutils.exception.DbException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/5/30
 */
public class GradeActivity extends Activity {

    private TextView mGrade_times;
    private TextView mNeed_grade;
    private TextView mRequired_course;
    private TextView mElective_course;
    private TextView mCurrent_grade;
    private ProgressBar mGrade_progress;
    private TextView mGrade_hint;
    private LocalTask mLocalTask;
    private NetThread mNetTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        initView();
        //初始化加载器
        mLocalTask = new LocalTask();
        mNetTask = new NetThread();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocalTask.execute();
        mNetTask.start();
    }

    private void initView() {
        mGrade_times = (TextView) findViewById(R.id.grade_times);
        mNeed_grade = (TextView) findViewById(R.id.need_grade);
        mRequired_course = (TextView) findViewById(R.id.required_course);
        mElective_course = (TextView) findViewById(R.id.elective_course);
        mCurrent_grade = (TextView) findViewById(R.id.current_grade);

        mGrade_progress = (ProgressBar) findViewById(R.id.grade_progress);
        mGrade_hint = (TextView) findViewById(R.id.grade_hint);

        Button grade_backBtn = (Button) findViewById(R.id.grade_backBtn);
        grade_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(GradeActivity.this, MainActivity.class));
                GradeActivity.this.finish();
            }
        });
    }

    class LocalTask extends AsyncTask<String, Integer, Integer> {

        String needScore;
        String currentScore;

        @Override
        protected void onPreExecute() {
            mGrade_progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {

            try {
                GradeInfo gradeInfo = MyApplication.sDbUtils.findFirst(GradeInfo.class);
                if (gradeInfo != null) {
                    //获取需修学分
                    needScore = gradeInfo.getNeedGrade();
                    //获取已修学分
                    currentScore = gradeInfo.getAlreadyGrade();
                    if (Integer.parseInt(needScore)!=-1&&Integer.parseInt(currentScore)!=-1){
                        return 1;
                    }

                } else {
                    return 2;
                }

            } catch (DbException e) {
                e.printStackTrace();
            }
            return 2;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            mGrade_progress.setVisibility(View.GONE);
            if (integer == 1) {
                mGrade_progress.setVisibility(View.GONE);
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                String time = sdf.format(date);

                mGrade_times.setText(time);
                mNeed_grade.setText(needScore);
                mCurrent_grade.setText(currentScore);
                mRequired_course.setText("--");
                mElective_course.setText("--");
            } else if (integer == 2) {
                mGrade_hint.setVisibility(View.VISIBLE);
                mGrade_hint.setText("十分抱歉，未查到有效数据。");
            }
        }
    }


    class NetThread extends Thread {
        @Override
        public void run() {
            String contentId = MyApplication.sSharedPreferences.getString(Constants.AUTO_LOGIN, null);
            BaseInfo.saveGrade(contentId);
        }
    }

}
