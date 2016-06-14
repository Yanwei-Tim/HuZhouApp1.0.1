package com.geekband.huzhouapp.nav;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.database.dto.DataOperation;
import com.database.pojo.UserInfoTable;
import com.database.pojo.UserTable;
import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.activity.MainActivity;
import com.geekband.huzhouapp.adapter.ManageListAdapter;
import com.geekband.huzhouapp.application.MyApplication;
import com.geekband.huzhouapp.utils.BaseInfo;
import com.geekband.huzhouapp.utils.Constants;
import com.geekband.huzhouapp.vo.ManageInfo;
import com.geekband.huzhouapp.vo.UserBaseInfo;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/19
 */
public class ManageActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView mManage_listView;
    private ProgressBar mManage_progress;
    private TextView mManage_back_textBtn;
    private TextView mUpdate_manage_btn;
    private boolean isUpdate;
    private ArrayList<ManageInfo> mManageList;
    private ScrollView mScrollView;
    private EditText mUpdate_realName;
    private EditText mUpdate_phoneNum;
    private EditText mUpdate_emailAddress;
    private EditText mUpdate_sex;
    private EditText mUpdate_address;
    private EditText mUpdate_birthday;
    private EditText mUpdate_card;
    private String mUserName;
    private String mRealName;
    private String mTelephone;
    private String mEmail;
    private String mSex;
    private String mAddress;
    private String mBirthday;
    private TextView mPrivate_info;
    private UserBaseInfo mUserBaseInfo;
    private UserTable mUserTable;
    private UserInfoTable mUserInfoTable;
    private TextView mManage_hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        isUpdate = true;

        mManage_back_textBtn = (TextView) findViewById(R.id.manage_back_textBtn);
        mUpdate_manage_btn = (TextView) findViewById(R.id.update_manage_btn);
        mPrivate_info = (TextView) findViewById(R.id.private_info);

        mManage_progress = (ProgressBar) findViewById(R.id.manage_progress);
        mManage_listView = (ListView) findViewById(R.id.manage_listView);

        mScrollView = (ScrollView) findViewById(R.id.update_layout);

        mUpdate_realName = (EditText) findViewById(R.id.update_realName);
        mUpdate_phoneNum = (EditText) findViewById(R.id.update_phoneNum);
        mUpdate_emailAddress = (EditText) findViewById(R.id.update_emailAddress);
        mUpdate_sex = (EditText) findViewById(R.id.update_sex);
        mUpdate_address = (EditText) findViewById(R.id.update_address);
        mUpdate_birthday = (EditText) findViewById(R.id.update_birthday);
        mUpdate_card = (EditText) findViewById(R.id.update_card);

        mManage_hint = (TextView) findViewById(R.id.manage_hint);

        mManage_listView.setOnItemClickListener(this);
        mManage_back_textBtn.setOnClickListener(this);
        mUpdate_manage_btn.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取用户信息
        new NetTask().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.manage_back_textBtn:
                if (!isUpdate) {
                    //加载浏览布局
                    mManage_listView.setVisibility(View.VISIBLE);
                    mScrollView.setVisibility(View.GONE);
                    ManageListAdapter manageListAdapter = new ManageListAdapter(mManageList, ManageActivity.this);
                    mManage_listView.setAdapter(manageListAdapter);
                    manageListAdapter.notifyDataSetChanged();

                    //更改btn
                    mPrivate_info.setText("个人信息");
                    mManage_back_textBtn.setText("返回");
                    mUpdate_manage_btn.setText("编辑");
                    isUpdate = true;
                } else {
                    startActivity(new Intent(this, MainActivity.class));
                    this.finish();
                    isUpdate = false;
                }

                break;
            case R.id.update_manage_btn:
                if (isUpdate) {
                    if (mUserBaseInfo!=null) {
                        //加载编辑布局
                        mManage_listView.setVisibility(View.GONE);
                        mScrollView.setVisibility(View.VISIBLE);
                        mUpdate_realName.setText(mRealName);
                        mUpdate_phoneNum.setText(mTelephone);
                        mUpdate_emailAddress.setText(mEmail);
                        mUpdate_sex.setText(mSex);
                        mUpdate_address.setText(mAddress);
                        mUpdate_birthday.setText(mBirthday);
                        //更改btn
                        mPrivate_info.setText("编辑信息");
                        mManage_back_textBtn.setText("取消");
                        mUpdate_manage_btn.setText("保存");
                        isUpdate = false;
                    }else {
                        Toast.makeText(this,"请登录官网补充相关资料",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    //保存修改后的信息
                   new saveInfo().execute();
                    //标记
                    isUpdate = true;

                }
                break;

        }
    }

    //查询用户详细信息
    class LocalTask extends AsyncTask<String, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            mManage_progress.setVisibility(View.VISIBLE);
            mManage_listView.setVisibility(View.GONE);
            mScrollView.setVisibility(View.GONE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                mUserBaseInfo =  MyApplication.sDbUtils.findFirst(UserBaseInfo.class);

            } catch (DbException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            mManage_progress.setVisibility(View.GONE);
            if (mUserBaseInfo!=null) {
                mManage_listView.setVisibility(View.VISIBLE);
                //初始化用户信息
                mUserName = mUserBaseInfo.getUserName();
                mRealName = mUserBaseInfo.getRealName();
                mTelephone = mUserBaseInfo.getPhoneNum();
                mEmail = mUserBaseInfo.getEmailAddress();

                mSex = mUserBaseInfo.getSex();
                mAddress = mUserBaseInfo.getAddress();
                mBirthday = mUserBaseInfo.getBirthday();

                mManageList = new ArrayList<>();
                mManageList.add(new ManageInfo("用户名称", mUserName, R.drawable.app_ailistview_item_three_back));
                mManageList.add(new ManageInfo("真实姓名", mRealName, R.drawable.app_ailistview_item_three_back));
                mManageList.add(new ManageInfo("手机号码", mTelephone, R.drawable.app_ailistview_item_three_back));
                mManageList.add(new ManageInfo("邮箱地址", mEmail, R.drawable.app_ailistview_item_three_back));

                mManageList.add(new ManageInfo("性别", mSex, R.drawable.app_ailistview_item_three_back));
                mManageList.add(new ManageInfo("地址", mAddress, R.drawable.app_ailistview_item_three_back));
                mManageList.add(new ManageInfo("生日", mBirthday, R.drawable.app_ailistview_item_three_back));

                ManageListAdapter manageListAdapter = new ManageListAdapter(mManageList, ManageActivity.this);
                mManage_listView.setAdapter(manageListAdapter);
                manageListAdapter.notifyDataSetChanged();
                isUpdate = true;
            }else {
                mManage_hint.setVisibility(View.VISIBLE);
            }

        }
    }

    class UpdateTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            Toast.makeText(ManageActivity.this, "正在保存信息", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            DataOperation.insertOrUpdateTable(mUserTable, null);
            DataOperation.insertOrUpdateTable(mUserInfoTable, null);
            String contentId = MyApplication.sSharedPreferences.getString(Constants.AUTO_LOGIN, null);
            BaseInfo.saveUserBaseInfo(contentId);
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Toast.makeText(ManageActivity.this, "保存完毕", Toast.LENGTH_LONG).show();
            //加载更新后的浏览布局
            mScrollView.setVisibility(View.GONE);
            //获取用户信息
            new LocalTask().execute();
            //更改btn
            mPrivate_info.setText("个人信息");
            mManage_back_textBtn.setText("返回");
            mUpdate_manage_btn.setText("编辑");
            isUpdate = true;
        }
    }

    /**
     * 保存修改后的信息
     */

    public void updateInfo() {
        //保存修改后的资料信息
        //userTable
        if (!mUpdate_realName.getText().toString().equals(mRealName)) {
            mUserTable.getRecord().putField(UserTable.FIELD_REALNAME, mUpdate_realName.getText().toString());
        }
        if (!mUpdate_phoneNum.getText().toString().equals(mTelephone)) {
            mUserTable.getRecord().putField(UserTable.FIELD_TELEPHONE, mUpdate_phoneNum.getText().toString());
        }
        if (!mUpdate_emailAddress.getText().toString().equals(mEmail)) {
            mUserTable.getRecord().putField(UserTable.FIELD_EMAIL, mUpdate_emailAddress.getText().toString());
        }
        //userTableInfo
        if (!mUpdate_sex.getText().toString().equals(mSex)) {
            mUserInfoTable.getRecord().putField(UserInfoTable.FIELD_SEX, mUpdate_sex.getText().toString());
        }
        if (!mUpdate_address.getText().toString().equals(mAddress)) {
            mUserInfoTable.getRecord().putField(UserInfoTable.FIELD_ADDRESS, mUpdate_address.getText().toString());
        }
        if (!mUpdate_birthday.getText().toString().equals(mBirthday)) {
            mUserInfoTable.getRecord().putField(UserInfoTable.FIELD_BIRTHDAY, mUpdate_birthday.getText().toString());
        }
    }

    class saveInfo extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            String contentId = MyApplication.sSharedPreferences.getString(Constants.AUTO_LOGIN, null);
            mUserTable = DataOperation.queryUserTableFormContentId(contentId);
            mUserInfoTable = DataOperation.queryUserInfoTable(contentId);
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            //编辑信息
            if (mUserTable!=null&&mUserInfoTable!=null) {
                updateInfo();
                //更新信息
                new UpdateTask().execute();
            }else {
                Toast.makeText(ManageActivity.this,"链接服务器失败",Toast.LENGTH_SHORT).show();
            }
        }
    }


    class NetTask extends AsyncTask<String,Integer,Integer>{

        @Override
        protected void onPreExecute() {
            new LocalTask().execute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            String contentId = MyApplication.sSharedPreferences.getString(Constants.AUTO_LOGIN,null);
            BaseInfo.saveUserBaseInfo(contentId);
            return null;
        }
    }

}
