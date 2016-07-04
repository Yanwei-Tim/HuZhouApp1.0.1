package com.geekband.huzhouapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.utils.Constants;
import com.geekband.huzhouapp.vo.DynamicNews;

/**
 * Created by Administrator on 2016/5/20
 */
public class NewsContentActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        Intent intent = getIntent();
        DynamicNews dynamicNews = new DynamicNews();
        switch (intent.getAction()) {
            case Constants.ROLLING:
                dynamicNews = intent.getParcelableExtra(Constants.ROLLING);
                break;
            case Constants.UNROLLING:
                dynamicNews = intent.getParcelableExtra(Constants.UNROLLING);
                break;
        }
        TextView content_title = (TextView) findViewById(R.id.content_title);
        TextView content_date = (TextView) findViewById(R.id.content_date);
        TextView content_auditor = (TextView) findViewById(R.id.content_auditor);
        TextView content_writer = (TextView) findViewById(R.id.content_writer);

        String title = dynamicNews.getTitle();
        String writer = "发布人:"+dynamicNews.getWriterId();
        String auditor = "审核处:"+dynamicNews.getAuditorId();
        String date = "发布时间:"+dynamicNews.getDate();


        content_title.setText(title);
        content_date.setText(date);
        content_auditor.setText(auditor);
        content_writer.setText(writer);

        WebView webView = (WebView) findViewById(R.id.newsContentWebView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setDomStorageEnabled(true);

        webView.setBackgroundColor(0);
        webView.getBackground().setAlpha(0);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        String htmlStr = "<html>" + "<body>" + dynamicNews.getContent() + "</body>" + "</html>";
        webView.loadDataWithBaseURL(null, htmlStr, "text/html", "utf-8", null);

        webView.setVisibility(View.VISIBLE);
    }

}
