package com.geekband.huzhouapp.fragment.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.utils.Constants;
import com.geekband.huzhouapp.vo.CourseInfo;

/**
 * Created by Administrator on 2016/5/29
 */
public class CourseContentActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_content);

        Intent intent = getIntent();
        CourseInfo courseInfo = intent.getParcelableExtra(Constants.COURSE_CONTENT);

        if (courseInfo!=null) {
            TextView course_name = (TextView) findViewById(R.id.course_name);
            TextView course_type = (TextView) findViewById(R.id.course_type);
            TextView course_time = (TextView) findViewById(R.id.course_time);
            TextView course_intro = (TextView) findViewById(R.id.course_intro);
            TextView course_detailed = (TextView) findViewById(R.id.course_detailed);

            course_name.setText(courseInfo.getTitle());
            course_type.setText(courseInfo.getType());
            course_time.setText(courseInfo.getTime());
            course_intro.setText(courseInfo.getIntro());
            course_detailed.setText(courseInfo.getDetailed());
        }
    }
}
