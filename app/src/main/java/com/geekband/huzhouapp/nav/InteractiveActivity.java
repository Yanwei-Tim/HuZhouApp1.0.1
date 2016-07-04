package com.geekband.huzhouapp.nav;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.baseadapter.CommonRecyclerAdapter;
import com.geekband.huzhouapp.baseadapter.CommonRecyclerViewHolder;
import com.geekband.huzhouapp.utils.BitmapHelper;
import com.geekband.huzhouapp.utils.DataUtils;
import com.geekband.huzhouapp.vo.BirthdayInfo;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/29
 */
public class InteractiveActivity extends Activity {

    private static final int PULL_TO_REFRESH = 1;//下拉刷新
    private static final int PULL_TO_LOAD = 2;//上拉加载
    int pageSize = 10;
    int currentPage =1;
    private ArrayList<BirthdayInfo> mList;
    private PtrClassicFrameLayout mPtr;
    private RecyclerAdapterWithHF mAdapterWithHF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interactive);
        mList = new ArrayList<>();
        initView();
    }

    private void initView() {
        final BitmapUtils bitmapUtils = BitmapHelper.getBitmapUtils(this,null,R.drawable.head_default,R.drawable.head_default);
        mPtr = (PtrClassicFrameLayout) findViewById(R.id.ptrLayout_birthday);
        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView_birthday);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
        CommonRecyclerAdapter adapter = new CommonRecyclerAdapter<BirthdayInfo>(this, R.layout.item_birthday,mList) {

            @Override
            public void convertView(CommonRecyclerViewHolder holder, BirthdayInfo birthdayInfo) {
                ImageView imageView = holder.getView(R.id.avatar_birthday);
                bitmapUtils.display(imageView,birthdayInfo.getAvatarImage());
                holder.setText(R.id.name_birthday, birthdayInfo.getRealName());
                holder.setText(R.id.content_birthday,"距离生日还有"+birthdayInfo.getDate()+"天");
            }
        };

        mAdapterWithHF = new RecyclerAdapterWithHF(adapter);
        rv.setAdapter(mAdapterWithHF);
        mPtr.setLastUpdateTimeRelateObject(this);
        mPtr.setResistance(1.7f);
        mPtr.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtr.setDurationToClose(200);
        mPtr.setDurationToCloseHeader(1000);
        mPtr.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtr.autoRefresh();
            }
        }, 100);

        mPtr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                new Thread() {
                    @Override
                    public void run() {
                        currentPage = 1;
                        mList.clear();
                        mList.addAll(DataUtils.getBirthdayInfo(pageSize, currentPage));
                        Message message = mHandler.obtainMessage();
                        message.what = PULL_TO_REFRESH;
                        mHandler.sendMessage(message);
                    }
                }.start();
            }
        });

        mPtr.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                currentPage += 1;
                mList.addAll(DataUtils.getBirthdayInfo(pageSize, currentPage));
                Message message = mHandler.obtainMessage();
                message.what = PULL_TO_LOAD;
                mHandler.sendMessage(message);
            }
        });

    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case PULL_TO_REFRESH:
                    mAdapterWithHF.notifyDataSetChanged();
                    mPtr.refreshComplete();
                    mPtr.setLoadMoreEnable(true);
                    break;
                case PULL_TO_LOAD:
                    mAdapterWithHF.notifyDataSetChanged();
                    mPtr.loadMoreComplete(true);
                    break;
            }

            return false;
        }
    });
}
