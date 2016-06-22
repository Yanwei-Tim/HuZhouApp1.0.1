package com.geekband.huzhouapp.utils;

import android.view.View;
import android.widget.EditText;

/**
 * Created by Administrator on 2016/6/20.
 */
public class ViewUtils {
    /**
     * spinner获取焦点时隐藏hint
     * @return
     */
    public static View.OnFocusChangeListener getFocusChangeListener() {
        View.OnFocusChangeListener mFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText editText = (EditText) v;
                String hint;
                if (hasFocus) {
                    hint = editText.getHint().toString();
                    editText.setTag(hint);
                    editText.setHint(null);
                } else {
                    hint = editText.getTag().toString();
                    editText.setHint(hint);
                }
            }
        };
        return mFocusChangeListener;
    }
}
