package com.geekband.huzhouapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.utils.Constants;
import com.geekband.huzhouapp.vo.LocalNews;

/**
 * Created by Administrator on 2016/5/20
 */
public class NewsContentActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        Intent intent = getIntent();
        LocalNews localNews = (LocalNews) intent.getSerializableExtra(Constants.LOCAL_NEWS);

        TextView content_title = (TextView) findViewById(R.id.content_title);
        TextView content_date = (TextView) findViewById(R.id.content_date);
        TextView news_content = (TextView) findViewById(R.id.news_content);

        content_title.setText(localNews.getTitle());
        content_date.setText(localNews.getDate());
        news_content.setText(localNews.getContent());
    }

}
