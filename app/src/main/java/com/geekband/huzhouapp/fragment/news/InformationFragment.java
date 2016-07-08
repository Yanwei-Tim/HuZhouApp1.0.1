package com.geekband.huzhouapp.fragment.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.activity.MainActivity;
import com.geekband.huzhouapp.activity.NewsContentActivity;
import com.geekband.huzhouapp.baseadapter.CommonRecyclerAdapter;
import com.geekband.huzhouapp.baseadapter.CommonRecyclerViewHolder;
import com.geekband.huzhouapp.utils.Constants;
import com.geekband.huzhouapp.utils.DataUtils;
import com.geekband.huzhouapp.vo.DynamicNews;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/12
 * 考勤模块
 */
public class InformationFragment extends Fragment implements RecyclerAdapterWithHF.OnItemClickListener {
    MainActivity mMainActivity;
    private static final int PULL_TO_REFRESH = 1;
    private static final int PULL_TO_LOAD = 2;
    private int currentPage = 0;
    private int pageSize = 15;
    private ArrayList<DynamicNews> mDynamicNewses;
    private PtrClassicFrameLayout mPtr;
    private RecyclerAdapterWithHF mAdapterWithHF;

    public static InformationFragment newInstance() {
        return new InformationFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, null);
        mDynamicNewses = new ArrayList<>();
        initView(view);
        return view;
    }

    private void initView(View view) {
        mPtr = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_frameLayout_information);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recycler_view_information);
        LinearLayoutManager manager = new LinearLayoutManager(mMainActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
        CommonRecyclerAdapter commonRecyclerAdapter = new CommonRecyclerAdapter<DynamicNews>(getActivity(), R.layout.information_item, mDynamicNewses) {
            @Override
            public void convertView(CommonRecyclerViewHolder holder, DynamicNews dynamicNews) {
                holder.getView(R.id.information_title_item).setSelected(true);
                holder.setText(R.id.information_title_item, dynamicNews.getTitle());
                holder.setText(R.id.information_date_item, dynamicNews.getDate());
            }
        };
        //noinspection unchecked
        mAdapterWithHF = new RecyclerAdapterWithHF(commonRecyclerAdapter);
        rv.setAdapter(mAdapterWithHF);
        mAdapterWithHF.setOnItemClickListener(this);
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
                        currentPage = 0;
                        mDynamicNewses.clear();
                        ArrayList<DynamicNews> dynamicNewses = DataUtils.getInformationInfo(currentPage, pageSize);
                        if (dynamicNewses != null) {
                            mDynamicNewses.addAll(dynamicNewses);
                            // System.out.println("view数据:"+mDynamicNewses);
                            Message message = mHandler.obtainMessage();
                            message.what = PULL_TO_REFRESH;
                            mHandler.sendMessage(message);
                        }

                    }
                }.start();
            }
        });
        mPtr.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                new Thread() {
                    @Override
                    public void run() {
                        currentPage += 1;
                        ArrayList<DynamicNews> moreList = DataUtils.getInformationInfo(currentPage, pageSize);
                        if (moreList != null) {
                            mDynamicNewses.addAll(moreList);
                            Message message = mHandler.obtainMessage();
                            message.what = PULL_TO_LOAD;
                            mHandler.sendMessage(message);
                        }
                    }
                }.start();
            }
        });
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
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

    @Override
    public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
        DynamicNews dynamicNews = mDynamicNewses.get(position);
        Intent intent = new Intent();
        intent.setClass(mMainActivity, NewsContentActivity.class);
        intent.setAction(Constants.INFORMATION_CONTENT);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.INFORMATION_CONTENT, dynamicNews);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
