package com.geekband.huzhouapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.utils.BitmapHelper;
import com.geekband.huzhouapp.vo.AlbumInfo;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/23
 */
public class GalleryGridAdapter extends BaseAdapter  {
    Context mContext;
    ArrayList<AlbumInfo> mAlbumInfoList;

    /**
     * Image Url的数组
     */
    private ArrayList<String> mImageThumbUrls;

    /**
     * GridView对象的应用
     */
    private GridView mGridView;
    //下载网络图片
    BitmapUtils mBitmapUtils;

    /**
     * 记录是否刚打开程序，用于解决进入程序不滚动屏幕，不会下载图片的问题。
     * 参考http://blog.csdn.net/guolin_blog/article/details/9526203#comments
     */
    private boolean isFirstEnter;

    /**
     * 一屏中第一个item的位置
     */
    private int mFirstVisibleItem;

    /**
     * 一屏中所有item的个数
     */
    private int mVisibleItemCount;

    public GalleryGridAdapter(Context context, GridView gridView, ArrayList<AlbumInfo> albumList, ArrayList<String> imageThumbUrls) {
        mContext = context;
        mAlbumInfoList = albumList;
        mGridView = gridView;
        mImageThumbUrls = imageThumbUrls;
        mBitmapUtils = new BitmapUtils(mContext);
        mBitmapUtils = BitmapHelper.getBitmapUtils(mContext,mGridView,R.drawable.app_icon_face,0);
    }

    @Override
    public int getCount() {
        return mAlbumInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAlbumInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final String mImageUrl = mImageThumbUrls.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_album, null);
            viewHolder.item_albumName = (TextView) convertView.findViewById(R.id.item_albumName);
            viewHolder.item_albumCount = (TextView) convertView.findViewById(R.id.item_albumCount);
            viewHolder.item_albumImage = (ImageView) convertView.findViewById(R.id.item_albumImage);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.item_albumName.setText(mAlbumInfoList.get(position).getAlbumName());
        viewHolder.item_albumCount.setText(String.valueOf(mAlbumInfoList.get(position).getAlbumCount()));
        //给ImageView设置Tag,这里已经是司空见惯了
        viewHolder.item_albumImage.setTag(mImageUrl);
        mBitmapUtils.display(viewHolder.item_albumImage, mImageUrl,new CustomBitmapLoadCallBack(viewHolder));
        return convertView;
    }

//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        //仅当GridView静止时才去下载图片，GridView滑动时取消所有正在下载的任务
//        new PauseOnScrollListener(mBitmapUtils,false,true);
//        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
//            showImage(mFirstVisibleItem, mVisibleItemCount);
//        }
//    }
//
//    /**
//     * GridView滚动的时候调用的方法，刚开始显示GridView也会调用此方法
//     */
//
//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        mFirstVisibleItem = firstVisibleItem;
//        mVisibleItemCount = visibleItemCount;
//        showImage(mFirstVisibleItem, mVisibleItemCount);
//    }


    class ViewHolder {
        private TextView item_albumName;
        private TextView item_albumCount;
        private ImageView item_albumImage;
    }

    class CustomBitmapLoadCallBack extends DefaultBitmapLoadCallBack<ImageView>{
        private final ViewHolder mViewHolder;

        public CustomBitmapLoadCallBack(ViewHolder viewHolder) {
            mViewHolder = viewHolder;
        }
    }

//    /**
//     * 显示当前屏幕的图片，先会去查找LruCache，LruCache没有就去sd卡或者手机目录查找，在没有就开启线程去下载
//     * @param firstVisibleItem
//     * @param visibleItemCount
//     */
//    private void showImage(int firstVisibleItem, int visibleItemCount) {
//        for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
//            String mImageUrl = mImageThumbUrls.get(i);
//            final ImageView mImageView = (ImageView) mGridView.findViewWithTag(mImageUrl);
//            mBitmapUtils.configDefaultLoadFailedImage(R.drawable.failed_load);
//            mBitmapUtils.display(mImageView, mImageUrl);
//        }
//    }

}
