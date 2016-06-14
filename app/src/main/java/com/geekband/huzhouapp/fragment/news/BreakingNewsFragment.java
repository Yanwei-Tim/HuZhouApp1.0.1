package com.geekband.huzhouapp.fragment.news;

import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.activity.NewsContentActivity;
import com.geekband.huzhouapp.activity.WebViewActivity;
import com.geekband.huzhouapp.adapter.NewsRvAdapter;
import com.geekband.huzhouapp.application.MyApplication;
import com.geekband.huzhouapp.utils.BaseInfo;
import com.geekband.huzhouapp.utils.Constants;
import com.geekband.huzhouapp.utils.Paging;
import com.geekband.huzhouapp.vo.LocalNews;
import com.geekband.huzhouapp.vo.NetNewsInfo;
import com.lidroid.xutils.exception.DbException;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/12
 * 即时新闻模块
 */
public class BreakingNewsFragment extends Fragment implements
        OnItemClickListener, RecyclerAdapterWithHF.OnItemClickListener {

    public static final int UPDATE_BANNER = 0;
    private ConvenientBanner convenientBanner;//顶部广告栏控件
    //服务器新闻列表
    private ArrayList<String> mImageList;
    private ArrayList<String> mHtmlList;
    //数据库新闻列表
    private ArrayList<LocalNews> mLocalNewsList;

    //标记第一次启动程序
    boolean isFirstEnter = true;
    private RecyclerView mRecyclerView;
    private PtrClassicFrameLayout mPtrClassicFrameLayout;
    private RecyclerAdapterWithHF mAdapterWithHF;
    //当前页是第一页
    private int currentPage = 1;
    private ArrayList<LocalNews> mDisplayList;


    public static BreakingNewsFragment newInstance() {
        return new BreakingNewsFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_breaking_news, null);
        convenientBanner = (ConvenientBanner) view.findViewById(R.id.convenientBanner);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.breaking_news_list);
        mPtrClassicFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.rotate_header_list_view_frame);
        mLocalNewsList = new ArrayList<>();
        mDisplayList = new ArrayList<>();
        configPtr();
//        //加载本地数据
//        new DbNewsTask().execute();
        //加载网络数据
        new ReLoadTask().execute();
        //加载轮播新闻
        new BannerThread().start();


        return view;
    }


    private void initBannerView() {
        initImageLoader();

//    网络加载图片
//        mImageList = Arrays.asList(images);
//        System.out.println("网络加载测试：" + mImageList);
        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, mImageList)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设

                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                        //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .setOnItemClickListener(this);

    }

    //初始化网络图片缓存库
    private void initImageLoader() {
        //网络图片例子,结合常用的图片缓存库UIL,你可以根据自己需求自己换其他网络图片库
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
                showImageForEmptyUri(R.drawable.ic_default_adimage)
                .cacheInMemory(true).cacheOnDisc(true).build();
        if (getActivity() != null) {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                    getActivity().getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
                    .discCacheFileNameGenerator(new Md5FileNameGenerator())
                    .tasksProcessingOrder(QueueProcessingType.LIFO).build();
            ImageLoader.getInstance().init(config);
        }
    }


    // 开始自动翻页
    @Override
    public void onResume() {
        super.onResume();

        //开始自动翻页
        convenientBanner.startTurning(5000);

    }

    // 停止自动翻页
    @Override
    public void onPause() {
        super.onPause();
        //停止翻页
        convenientBanner.stopTurning();
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra("html", mHtmlList.get(position));
        startActivity(intent);
    }


    //获取服务器数据
    public ArrayList<LocalNews> getLocalNewsData() {
        ArrayList<LocalNews> localNewses = null;
        try {
            localNewses = (ArrayList<LocalNews>) MyApplication.sDbUtils.findAll(LocalNews.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return localNewses;
    }


    //加载服务器新闻
    class ReLoadTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            BaseInfo.saveNetNews();
            BaseInfo.saveLocalNews();
            return null;
        }

    }

    //加载本地数据库新闻
    class DbNewsTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            //加载服务器新闻
            mLocalNewsList.addAll(getLocalNewsData());
            if (mLocalNewsList != null) {
                return 1;
            }
            return 2;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            //初始化本地新闻列表
            if (integer == 1) {
                //允许显示的数据列表
                mDisplayList.addAll(getNewsList(mLocalNewsList));
                mAdapterWithHF.notifyDataSetChanged();
                mPtrClassicFrameLayout.refreshComplete();
                mPtrClassicFrameLayout.setLoadMoreEnable(true);
            } else {
                Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //加载轮播新闻
    class BannerThread extends Thread {
        @Override
        public void run() {
            try {
                ArrayList<NetNewsInfo> netNewsInfoList = (ArrayList<NetNewsInfo>)
                        MyApplication.sDbUtils.findAll(NetNewsInfo.class);

                //发送更新界面的请求
                Message message = Message.obtain();
                message.what = UPDATE_BANNER;
                message.obj = netNewsInfoList;
                mHandler.sendMessage(message);

            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_BANNER:
                    ArrayList<NetNewsInfo> netNewsInfoList = (ArrayList<NetNewsInfo>) msg.obj;
                    //加载轮播新闻
                    mImageList = new ArrayList<>();
                    mHtmlList = new ArrayList<>();
                    if (netNewsInfoList != null) {
                        for (NetNewsInfo netNewsInfo : netNewsInfoList) {
                            mImageList.add(netNewsInfo.getNewsPic());
                            mHtmlList.add(netNewsInfo.getNewsHTML());
                        }
                        //更新轮播新闻界面
                        initBannerView();
                    }
                    break;
                default:
                    break;

            }
            return false;
        }
    });

    //初始化本地新闻列表
    private void configPtr() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(layoutManager);
            //RecyclerView自定义Adapter
            NewsRvAdapter rvAdapter = new NewsRvAdapter(mDisplayList, getActivity());
            mAdapterWithHF = new RecyclerAdapterWithHF(rvAdapter);
            mRecyclerView.setAdapter(mAdapterWithHF);
            mAdapterWithHF.setOnItemClickListener(this);

            if (mPtrClassicFrameLayout != null) {
                //下拉刷新支持时间
                mPtrClassicFrameLayout.setLastUpdateTimeRelateObject(this);
                //下拉刷新其他基本设置
                mPtrClassicFrameLayout.setResistance(1.7f);
                mPtrClassicFrameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
                mPtrClassicFrameLayout.setDurationToClose(200);
                mPtrClassicFrameLayout.setDurationToCloseHeader(1000);
                // default is false
                mPtrClassicFrameLayout.setPullToRefresh(false);
                // default is true
                mPtrClassicFrameLayout.setKeepHeaderWhenRefresh(true);
                //进入activity自动下拉刷新
                mPtrClassicFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrClassicFrameLayout.autoRefresh();
                    }
                }, 100);

                //下拉刷新
                mPtrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
                    @Override
                    public void onRefreshBegin(PtrFrameLayout frame) {
                        //重载数据
                        mDisplayList.clear();
                        //从头开始加载
                        currentPage=1;
                        new DbNewsTask().execute();
//                        mAdapterWithHF.notifyDataSetChanged();
//                        mPtrClassicFrameLayout.refreshComplete();
//                        mPtrClassicFrameLayout.setLoadMoreEnable(true);

                    }
                });

                //上拉加载
                mPtrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void loadMore() {
                        //加载更新数据
                        currentPage += 1;
                        mDisplayList.addAll(getNewsList(mLocalNewsList));
                        mAdapterWithHF.notifyDataSetChanged();
                        mPtrClassicFrameLayout.loadMoreComplete(true);
                    }
                });
            }

        }
    }

    //分页加载
    public ArrayList<LocalNews> getNewsList(ArrayList<LocalNews> list) {
        ArrayList<LocalNews> subNewsList = new ArrayList<>();
        Paging paging = new Paging(10, list.size(), currentPage);
        int start = paging.getFromIndex();
        int end = paging.getToIndex();
        subNewsList.addAll(list.subList(start, end));
        return subNewsList;
    }

    //点击进入服务器信息详情内容界面
    @Override
    public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
        LocalNews localNews = mDisplayList.get(position);
        if (localNews==null){
            mDisplayList.remove(position);
        }
        Intent intent = new Intent(getActivity(), NewsContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.LOCAL_NEWS, localNews);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
