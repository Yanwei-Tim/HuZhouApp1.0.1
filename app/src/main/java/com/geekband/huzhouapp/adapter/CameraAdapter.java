package com.geekband.huzhouapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.utils.BitmapHelper;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/6
 */
public class CameraAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<String> mArrayList;
    private BitmapUtils mBitmapUtils;
    private GridView mGridView;
    private int mFirstVisibleItem;
    private int mVisibleItemCount;

    public CameraAdapter(Context context, GridView gridView, ArrayList<String> arrayList) {
        mContext = context;
        mArrayList = arrayList;
        mGridView = gridView;
        mBitmapUtils = BitmapHelper.getBitmapUtils(mContext,mGridView,R.drawable.app_icon_face,0);
    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        String mImageUrl = mArrayList.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_camera_list, null);
            viewHolder.imageView_camera = (ImageView) convertView.findViewById(R.id.imageView_camera);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //给图片设置标签
        viewHolder.imageView_camera.setTag(mImageUrl);
        mBitmapUtils.display(viewHolder.imageView_camera, mImageUrl,new CustomBitmapLoadCallBack(viewHolder));
        return convertView;
    }

//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
//            Log.i("静止加载图片：","开始加载");
//            showImage(mFirstVisibleItem, mVisibleItemCount);
//        } else {
//            Log.i("滚动加载图片：", "停止加载");
//        }
//    }

//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        mFirstVisibleItem = firstVisibleItem;
//        mVisibleItemCount = visibleItemCount;
//        if (visibleItemCount > 0) {
//            showImage(mFirstVisibleItem, mVisibleItemCount);
//        }
//    }

    class ViewHolder {
        private ImageView imageView_camera;
    }

    class CustomBitmapLoadCallBack extends DefaultBitmapLoadCallBack<ImageView>{
        private final ViewHolder mViewHolder;

        public CustomBitmapLoadCallBack(ViewHolder viewHolder) {
            mViewHolder = viewHolder;
        }
    }

//    public void showImage(int firstVisibleItem, int visibleItemCount) {
//        for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
//            String imageUrl = mArrayList.get(i);
//            ImageView imageView = (ImageView) mGridView.findViewWithTag(imageUrl);
//            mBitmapUtils.display(imageView, imageUrl);
//        }
//    }
}
