package com.chat.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.chat.adapter.QuestionListAdapter;
import com.chat.adapter.pojo.Answer;
import com.chat.adapter.pojo.Question;
import com.database.dto.DataOperation;
import com.database.pojo.EnquiryTable;
import com.database.pojo.ReplyTable;
import com.database.pojo.UserTable;
import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.application.MyApplication;
import com.geekband.huzhouapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * 当前用户的提问列表Activity
 */
public class PersonalQuestionActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton btn_back;
    private ViewGroup vg_progress;
    private ListView lv_personalQuestionListView;
    private QuestionListAdapter questionListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_personalquestion);
        findView();
        initVar();
        initView();
        initListener();
        runAsyncTask(AsyncDataLoader.TASK_INITLISTVIEW);
    }

    private void findView() {
        btn_back = (ImageButton) findViewById(R.id.btn_personalquestion_back);
        vg_progress = (ViewGroup) findViewById(R.id.vg_personalquestion_progress);
        lv_personalQuestionListView = (ListView) findViewById(R.id.lv_personalquestion_questionList);
    }

    private void initView() {
        lv_personalQuestionListView.setAdapter(questionListAdapter);
    }

    private void initVar() {
        questionListAdapter = new QuestionListAdapter(this);
    }

    private void initListener() {
        btn_back.setOnClickListener(this);

        lv_personalQuestionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(PersonalQuestionActivity.this, QuestionDetailActivity.class);
                intent.putExtra(QuestionDetailActivity.ARGS_QUESTION, questionListAdapter.getItem(position));
                startActivity(intent);
            }
        });
    }

    private void runAsyncTask(int task, Object... params) {
        new AsyncDataLoader(task, params).execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_personalquestion_back: {
                finish();
            }
            break;

        }
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
                    vg_progress.setVisibility(View.VISIBLE);
                    lv_personalQuestionListView.setVisibility(View.GONE);
                }
                break;
            }
        }

        @Override
        protected Integer doInBackground(Object... params) {
            switch (task) {
                case TASK_INITLISTVIEW: {
                    try {
                        //当前用户ContentId
                        String currentUserContentId = MyApplication.sSharedPreferences.getString(Constants.AUTO_LOGIN, "");
                        //当前用户(即提问者)
                        UserTable asker = (UserTable) DataOperation.queryTable(UserTable.TABLE_NAME, UserTable.CONTENTID, currentUserContentId).get(0);
                        //当前用户的问题列表
                        List<EnquiryTable> askList = (ArrayList<EnquiryTable>) DataOperation.queryTable(EnquiryTable.TABLE_NAME, EnquiryTable.FIELD_USERNO, currentUserContentId); //用户提问

                        //把问题列表数据添加到listView的数据源中
                        if (askList != null) {
                            for (EnquiryTable ask : askList) //每一条问题
                            {
                                List<ReplyTable> replyList = (List<ReplyTable>) DataOperation.queryTable(ReplyTable.TABLE_NAME, ReplyTable.FIELD_REPLYTONO, ask.getContentId());
                                List<Answer> answerList = new ArrayList<>();

                                if (replyList != null) {
                                    for (ReplyTable replyTable : replyList) //问题的每一条回答
                                    {
                                        answerList.add(new Answer((UserTable) DataOperation.queryTable(UserTable.TABLE_NAME, UserTable.CONTENTID, replyTable.getField(ReplyTable.FIELD_USERNO)).get(0), replyTable));
                                    }
                                }

                                questionListAdapter.getQuestionList().add(new Question(asker, ask, answerList));
                            }
                        }

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
                    questionListAdapter.notifyDataSetChanged();
                    vg_progress.setVisibility(View.GONE);
                    lv_personalQuestionListView.setVisibility(View.VISIBLE);
                }
                break;

                case TASK_INITLISTVIEW_RESULT_ERROR: {
                    vg_progress.setVisibility(View.GONE);
                    lv_personalQuestionListView.setVisibility(View.VISIBLE);
                    Toast.makeText(PersonalQuestionActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
