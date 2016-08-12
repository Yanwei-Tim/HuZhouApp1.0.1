package com.geekband.huzhouapp.nav;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.baseadapter.CommonAdapter;
import com.geekband.huzhouapp.baseadapter.ViewHolder;
import com.geekband.huzhouapp.utils.BitmapHelper;
import com.geekband.huzhouapp.utils.DataUtils;
import com.geekband.huzhouapp.vo.DynamicNews;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/12
 */
public class ReceiveGiftListActivity extends Activity implements AdapterView.OnItemClickListener{

    private ListView mListView;
    private BitmapUtils mBitmapUtils;
    private ArrayList<DynamicNews> mDynamicNewses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_gift);

        mDynamicNewses = new ArrayList<>();
        findView();

        if (getIntent().getAction()!=null&&getIntent().getAction().equals("messageToActivity")){
            mDynamicNewses = getIntent().getParcelableArrayListExtra("giftMessageToActivity");
            initView();
        }else {
            new GiftMessageTask().execute();
        }

    }

    private void initView() {
            if (mDynamicNewses!=null&&mDynamicNewses.size()!=0){
                CommonAdapter<DynamicNews> commonAdapter = new CommonAdapter<DynamicNews>(this,mDynamicNewses,R.layout.item_birthday) {
                    @Override
                    public void convert(ViewHolder viewHolder, DynamicNews item) {
                        mBitmapUtils.display(viewHolder.getView(R.id.avatar_birthday),item.getPicUrl());
                        viewHolder.setText(R.id.name_birthday, item.getWriter());
                        viewHolder.setText(R.id.content_birthday,"给您发来生日祝福，点击查看吧");
                    }
                };
                mListView.setAdapter(commonAdapter);
            }


    }

    private void findView() {
        mBitmapUtils = BitmapHelper.getBitmapUtils(this, mListView, R.drawable.head_default, R.drawable.head_default);
        mListView = (ListView) findViewById(R.id.receiver_gift_list);
        mListView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(this, GiftMessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("giftMessage",mDynamicNewses.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    class GiftMessageTask extends AsyncTask<String,Integer,Integer>{

        @Override
        protected Integer doInBackground(String... params) {
            mDynamicNewses = DataUtils.getGiftInfo();
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            initView();
        }
    }
}
