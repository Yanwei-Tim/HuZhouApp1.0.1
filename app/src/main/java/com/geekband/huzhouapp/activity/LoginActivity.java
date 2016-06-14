package com.geekband.huzhouapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.database.dto.DataOperation;
import com.database.pojo.UserTable;
import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.application.MyApplication;
import com.geekband.huzhouapp.utils.BaseInfo;
import com.geekband.huzhouapp.utils.Constants;

/**
 * Created by Administrator on 2016/5/12
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Button mLogin_btn;
    private EditText mLogin_user_et;
    private EditText mLogin_password_et;
    private UserTable mUserTable;
    private ProgressBar mLogin_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLogin_btn = (Button) findViewById(R.id.login_btn);
        mLogin_progress = (ProgressBar) findViewById(R.id.login_progress);
        mLogin_user_et = (EditText) findViewById(R.id.login_user_et);
        mLogin_password_et = (EditText) findViewById(R.id.login_password_et);
        mLogin_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                new MyTask().execute();
                break;
        }
    }


    /**
     * @param et 输入信息
     * @return 用户名
     */
    public String getUserName(EditText et) {
        return et.getText().toString();
    }

    /**
     * @param et 输入信息
     * @return 用户密码
     */
    public String getUserPassword(EditText et) {
        return et.getText().toString();
    }

    public boolean isLogin(EditText name_et, EditText password_et) {
        mUserTable = DataOperation.queryUserTable(getUserName(name_et));
        if (mUserTable == null) {
            return false;
        } else {

            String password = mUserTable.getRecord().getField(UserTable.FIELD_PASSWORD);
            return password.equals(getUserPassword(password_et));
        }

    }

    class MyTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            mLogin_progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {

            if (mLogin_user_et != null && mLogin_password_et != null) {
                if (isLogin(mLogin_user_et, mLogin_password_et)) {
                    //标记自动登录,以用户contentId作为标记
                    SharedPreferences.Editor editor = MyApplication.sSharedPreferences.edit();
                    editor.putString(Constants.AUTO_LOGIN, mUserTable.getContentId());
                    editor.apply();
                    //缓存个人信息
                    BaseInfo.saveUserBaseInfo(mUserTable.getContentId());
                    //缓存相册信息
                    BaseInfo.saveAlbum(mUserTable.getContentId());
                    //缓存课程信息
                    BaseInfo.saveCourse(mUserTable.getContentId());
                    //获取学分
                    BaseInfo.saveGrade(mUserTable.getContentId());
                    return 1; //登录成功
                } else {
                    return 2;//用户或者密码错误
                }
            } else {
                return 3;//用户名或者密码为空
            }

        }

        @Override
        protected void onPostExecute(Integer integer) {
            mLogin_progress.setVisibility(View.GONE);
            if (integer == 1) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                LoginActivity.this.finish();
            } else if (integer == 2) {
                Toast.makeText(LoginActivity.this, "用户名或者密码错误", Toast.LENGTH_SHORT).show();
            } else if (integer == 3) {
                Toast.makeText(LoginActivity.this, "用户名或者密码不能为空", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
