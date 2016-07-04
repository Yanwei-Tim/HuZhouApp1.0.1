package com.geekband.huzhouapp.fragment.message;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.activity.MainActivity;

/**
 * Created by Administrator on 2016/5/12
 */
public class ContactsFragment extends Fragment {
    MainActivity mMainActivity;

    public static ContactsFragment newInstance(){
        return new ContactsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity= (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reply,null);
        return view;
    }
}
