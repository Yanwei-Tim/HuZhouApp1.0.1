package com.chat.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.chat.activity.ExpertActivity;
import com.chat.adapter.ExpertListAdapter;
import com.chat.adapter.pojo.Expert;
import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.custom.RefreshButton;
import com.geekband.huzhouapp.utils.DataOperationHelper;

import java.util.ArrayList;

public class ExpertFragment extends Fragment {

    private ExpertActivity parentActivity;
    private View v_rootView;
    private ListView lv_expertListView;
    private ViewGroup vg_progress;
    private ViewGroup vg_errorTip;
    private RefreshButton btn_refresh;

    private ExpertListAdapter expertListAdapter;
    private final String expertCategory;
    private boolean isInit;
    private Bundle fragmentStateValue;

    public ExpertFragment(String expertCategory) {
        this.expertCategory = expertCategory;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isInit = true; //判断是否是第一次创建该Fragment
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = (ExpertActivity) activity;
        fragmentStateValue = parentActivity.getBundle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v_rootView = inflater.inflate(R.layout.fragment_expert, container, false);

        findView();
        initVar();
        initView();
        initListener();

        //是否(自动刷新)初始化数据
        if (isInit) //只有当是第一次创建当前Fragment时；才需要自动刷新一次
        {
            btn_refresh.beginRefresh(); //实现vg_refresh的刷新方法后，在这里刷新数据
            isInit = false;
        }
        //下次再执行到这里的生命周期时，不是再从服务器上获取数据，而是从状态值中获取数据来恢复自己
        //因为此时数据已经获取到并且保存到状态值中了，所以无需再自动从服务器上读取
        else {
            //读取状态值，恢复自身状态
            if (fragmentStateValue.getSerializable(expertCategory) != null) {
                lv_expertListView.setVisibility(View.VISIBLE);
                expertListAdapter.getExpertList().addAll((ArrayList<Expert>) fragmentStateValue.getSerializable(expertCategory));
                expertListAdapter.notifyDataSetChanged();
            }
        }

        return v_rootView;
    }

    private void findView() {
        lv_expertListView = (ListView) v_rootView.findViewById(R.id.lv_expert_expertlist);
        vg_progress = (ViewGroup) v_rootView.findViewById(R.id.vg_expert_progress);
        vg_errorTip = (ViewGroup) v_rootView.findViewById(R.id.vg_expert_errorTip);
        btn_refresh = (RefreshButton) v_rootView.findViewById(R.id.vg_expert_refresh);
    }

    private void initView() {
        lv_expertListView.setAdapter(expertListAdapter);
        vg_progress.setVisibility(View.GONE);
        lv_expertListView.setVisibility(View.GONE);
        vg_errorTip.setVisibility(View.GONE);
    }

    private void initVar() {
        expertListAdapter = new ExpertListAdapter(getActivity(), lv_expertListView);
    }

    private void initListener() {
        lv_expertListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "查看详情", Toast.LENGTH_SHORT).show();
            }
        });

        btn_refresh.setRefreshListener(new RefreshButton.RefreshListener() {
            @Override
            public void onRefresh() {
                runAsyncTask(AsyncDataLoader.TASK_INITLISTVIEW);
            }
        });
    }

    private void runAsyncTask(int task, Object... params) {
        new AsyncDataLoader(task, params).execute();
    }

    private class AsyncDataLoader extends AsyncTask<Object, Integer, Integer> {
        private static final int TASK_INITLISTVIEW = 1;
        private static final int TASK_INITLISTVIEW_RESULT_SUCCESS = 1;
        private static final int TASK_INITLISTVIEW_RESULT_ERROR = -1;

        private int task;
        private Object[] params;

        public AsyncDataLoader(int task, Object... params) {
            this.task = task;
            this.params = params;
        }

        @Override
        protected void onPreExecute() {
            switch (task) {
                case TASK_INITLISTVIEW: {
                    if (lv_expertListView.getVisibility() == View.GONE)
                        vg_progress.setVisibility(View.VISIBLE);
                    vg_errorTip.setVisibility(View.GONE);
                }
                break;
            }
        }

        ArrayList<Expert> expertList = new ArrayList<>();

        @Override
        protected Integer doInBackground(Object... params) {
            switch (task) {
                case TASK_INITLISTVIEW: {
                    try {
                        expertList = DataOperationHelper.queryExpertList();
                        return TASK_INITLISTVIEW_RESULT_SUCCESS;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return TASK_INITLISTVIEW_RESULT_ERROR;
                    }
                }
            }

            return 0;
        }

        @Override
        protected void onPostExecute(Integer taskResult) {
            switch (taskResult) {
                case TASK_INITLISTVIEW_RESULT_SUCCESS: {
                    expertListAdapter.getExpertList().clear();
                    expertListAdapter.getExpertList().addAll(expertList);
                    expertList.clear();
                    expertListAdapter.notifyDataSetChanged();
                    btn_refresh.refreshComplete();
                    //将Fragment中的状态数据保存到其父Activity中
                    //然后可以在Fragment的生命周期中从其父Activity中获取数据恢复自己的状态
                    //使用expertCategory作为每个Fragment的标识符，因为该标识符对每个Fragment都是唯一且不可变的
                    fragmentStateValue.putSerializable(expertCategory, (ArrayList<Expert>) expertListAdapter.getExpertList());

                    lv_expertListView.setVisibility(View.VISIBLE);
                    vg_progress.setVisibility(View.GONE);
                    vg_errorTip.setVisibility(View.GONE);

                }
                break;

                case TASK_INITLISTVIEW_RESULT_ERROR: {
                    btn_refresh.refreshComplete();

                    if (expertListAdapter.getExpertList().size() == 0) {
                        lv_expertListView.setVisibility(View.GONE);
                        vg_errorTip.setVisibility(View.VISIBLE);
                    } else {
                        lv_expertListView.setVisibility(View.VISIBLE);
                        vg_errorTip.setVisibility(View.GONE);
                    }
                    vg_progress.setVisibility(View.GONE);
                }
                break;
            }
        }
    }
}
