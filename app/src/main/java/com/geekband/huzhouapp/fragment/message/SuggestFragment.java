package com.geekband.huzhouapp.fragment.message;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.database.dto.DataOperation;
import com.database.pojo.ContentTable;
import com.database.pojo.OpinionTable;
import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.activity.MainActivity;
import com.geekband.huzhouapp.application.MyApplication;
import com.geekband.huzhouapp.utils.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/5/12
 */
public class SuggestFragment extends Fragment {
    MainActivity mMainActivity;
    final String TAG = SuggestFragment.class.getSimpleName();
    private EditText mSuggest_edit;

    public static SuggestFragment newInstance() {
        return new SuggestFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggest, null);
        String content = getCustomText(R.raw.us);

        TextView text_suggest = (TextView) view.findViewById(R.id.text_suggest);
        text_suggest.setText(content);
        mSuggest_edit = (EditText) view.findViewById(R.id.suggest_edit);

        Button suggest_submit = (Button) view.findViewById(R.id.suggest_submit);

        suggest_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String submitInfo = mSuggest_edit.getText().toString();
                new SubmitTask().execute(submitInfo);
            }
        });

        return view;
    }

    public String  getCustomText(int id) {
        InputStream in = getResources().openRawResource(id);
        return  getString(in);
    }

    //一个获取InputStream中字符串内容的方法：
    public String getString(InputStream in) {
        InputStreamReader inputStreamReader;
        StringBuilder sb = new StringBuilder("");
        try {
            inputStreamReader = new InputStreamReader(in, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    class SubmitTask extends AsyncTask<String ,Integer,Integer>{
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(mMainActivity,null,"正在提交相关信息");
        }

        @Override
        protected Integer doInBackground(String... params) {
            //用户的contentId
            String contentId = MyApplication.sSharedPreferences.getString(Constants.AUTO_LOGIN,null);
            //投稿时间
            Date date = new Date();
            long time = date.getTime();
            Date d = new Date(time);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String postTime = sf.format(d);
            //插入一张需求建议表
            OpinionTable opinionTable = new OpinionTable();
            opinionTable.getRecord().putField(OpinionTable.FIELD_USERID, contentId);
            opinionTable.getRecord().putField(OpinionTable.FIELD_POSTTIME, postTime);
            //插入表单并获得表单的contentId
            String opinionIId = DataOperation.insertOrUpdateTable(opinionTable, null);
            Log.i(TAG,"opinionIId:"+opinionIId);
            Log.i(TAG,"contentId:"+contentId);
            //获取通用表并插入数据
            ContentTable contentTable = new ContentTable();
            contentTable.getRecord().putField(ContentTable.FIELD_SUBSTANCE,params[0]);
            contentTable.getRecord().putField(ContentTable.FIELD_NEWSID,opinionIId);
            DataOperation.insertOrUpdateTable(contentTable,null);
            Log.i(TAG, "params[0]:" + params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            pd.dismiss();
            mSuggest_edit.setText(null);
        }
    }
}
