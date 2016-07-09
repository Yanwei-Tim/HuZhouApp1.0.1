package com.geekband.huzhouapp.nav;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.database.dto.DataOperation;
import com.database.pojo.ContentTable;
import com.database.pojo.Document;
import com.database.pojo.OpinionTable;
import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.application.MyApplication;
import com.geekband.huzhouapp.utils.Constants;
import com.geekband.huzhouapp.utils.FileUtils;

/**
 * Created by Administrator on 2016/7/8
 */
public class GiftActivity extends Activity implements View.OnClickListener {

    private EditText mGift_edit;
    private ProgressDialog mPd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);
        initView();
    }

    private void initView() {
        TextView gift_back_textBtn = (TextView) findViewById(R.id.gift_back_textBtn);
        mGift_edit = (EditText) findViewById(R.id.gift_edit);
        TextView gift_send_btn = (TextView) findViewById(R.id.gift_send_btn);
        gift_send_btn.setOnClickListener(this);
        gift_back_textBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gift_back_textBtn:
                Intent intent = new Intent(this, InteractiveActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.gift_send_btn:
                String contentStr = mGift_edit.getText().toString();
                String receiverId = getIntent().getStringExtra(Constants.BIRTHDAY_GIFT);
                String userId = MyApplication.sSharedPreferences.getString(Constants.AUTO_LOGIN, null);
                new SendGiftTask().execute(contentStr, receiverId, userId);


        }
    }

    class SendGiftTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            mPd = ProgressDialog.show(GiftActivity.this, null, "正在发送");
        }

        @Override
        protected Integer doInBackground(String... params) {
            String contentStr = params[0];
            String receiverId = params[1];
            String userId = params[2];
            String postDate = FileUtils.getCurrentTimeStr();
            //插入一张需求建议表
            OpinionTable opinionTable = new OpinionTable();
            opinionTable.putField(OpinionTable.FIELD_USERID, receiverId);
            opinionTable.putField(OpinionTable.FIELD_POSTTIME, postDate);
            if (DataOperation.insertOrUpdateTable(opinionTable,(Document) null)){
                //获取通用表并插入数据
                ContentTable contentTable = new ContentTable();
                contentTable.putField(ContentTable.FIELD_SUBSTANCE,contentStr);
                contentTable.putField(ContentTable.FIELD_NEWSID, opinionTable.getContentId());
                contentTable.putField(ContentTable.FIELD_DIVI, userId);
                //这里可以选择放入贺卡
                DataOperation.insertOrUpdateTable(contentTable, (Document) null);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            mPd.dismiss();
        }
    }
}
