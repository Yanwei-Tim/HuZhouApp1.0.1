package com.geekband.huzhouapp.fragment.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.utils.Constants;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/29
 */
public class CourseContentActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_content);

        Intent intent = getIntent();
        ArrayList<CharSequence> cs = intent.getCharSequenceArrayListExtra(Constants.COURSE_CONTENT);

        if (cs!=null) {
            TextView course_name = (TextView) findViewById(R.id.course_name);
            TextView course_type = (TextView) findViewById(R.id.course_type);
            TextView course_time = (TextView) findViewById(R.id.course_time);
            TextView course_intro = (TextView) findViewById(R.id.course_intro);
            TextView course_detailed = (TextView) findViewById(R.id.course_detailed);

            course_name.setText(cs.get(0));
            course_type.setText(cs.get(1));
            course_time.setText(cs.get(2));
            course_intro.setText(cs.get(3));
            course_detailed.setText(cs.get(4));
        }
    }
}
