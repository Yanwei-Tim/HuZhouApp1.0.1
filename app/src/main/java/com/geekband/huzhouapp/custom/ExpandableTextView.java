package com.geekband.huzhouapp.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.geekband.huzhouapp.R;


public class ExpandableTextView extends TextView {
    private static int MIN_LINE_NUM = 2;
    private static int MAX_LINE_NUM = 20;
    private int lineNum = MIN_LINE_NUM;
    private boolean mIsExpanded = false;
    Bitmap bitmapDown;
    Bitmap bitmapUp;
    int w, h;

    private OnClickListener mOnClickListener;

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setMaxLines(lineNum);
        bitmapDown = BitmapFactory.decodeResource(getResources(), R.drawable.update_detail_down);
        bitmapUp = BitmapFactory.decodeResource(getResources(), R.drawable.update_detail_up);
        w = bitmapDown.getWidth() + (int) context.getResources().getDimension(R.dimen.expand_textView_right);
        h = bitmapDown.getHeight() + (int) context.getResources().getDimension(R.dimen.expand_textView_bottom);
        
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lineNum == MIN_LINE_NUM) {
                    lineNum = MAX_LINE_NUM;
                    setMaxLines(lineNum);
                } else {
                    lineNum = MIN_LINE_NUM;
                    setMaxLines(lineNum);
                }
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(v);
                }
            }
        });
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {
        if (!isClickable()) {
            setClickable(true);
        }
        mOnClickListener = listener;
    }

    @Override
    public void setMaxLines(int maxlines) {
        super.setMaxLines(maxlines);

        if (lineNum == MAX_LINE_NUM) {
            mIsExpanded = true;
        } else {
            mIsExpanded = false;
        }
    }

    public boolean isExpanded() {
        return mIsExpanded;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getLineCount() <= MIN_LINE_NUM) {
            super.onDraw(canvas);
            return;
        }
        if (lineNum == MIN_LINE_NUM) {
            canvas.drawBitmap(bitmapDown, getWidth() - w, getHeight() - h, null);
        } else {
            canvas.drawBitmap(bitmapUp, getWidth() - w, getHeight() - h, null);
        }
        super.onDraw(canvas);
    }


    public void setMaxLine(int line) {
        lineNum = line;
        setMaxLines(line);
    }
}
