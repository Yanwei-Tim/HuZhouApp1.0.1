package com.geekband.huzhouapp.nav;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.database.dto.DataOperation;
import com.database.pojo.AlbumTable;
import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.adapter.ImageAdapter;
import com.geekband.huzhouapp.adapter.RecyclerItemClickListener;
import com.geekband.huzhouapp.utils.BitmapHelper;
import com.geekband.huzhouapp.utils.Constants;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/24
 */
public class ImageActivity extends Activity {
    private ArrayList<String> mImageList;
    private ImageView mTop_imageView;
    private BitmapUtils mBitmapUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iamge);
        mImageList = new ArrayList<>();
        mTop_imageView = (ImageView) findViewById(R.id.top_imageView);
        mBitmapUtils = BitmapHelper.getBitmapUtils(this, null, 0, 0);
        new LoadImageTask().execute();

    }

    class LoadImageTask extends AsyncTask<String, Integer, Integer> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Integer doInBackground(String... params) {

            Intent intent = getIntent();
            String contentId = intent.getStringExtra(Constants.IMAGE_CONTENT_ID);
            //noinspection unchecked
            ArrayList<AlbumTable> albumTables = (ArrayList<AlbumTable>) DataOperation.queryTable(AlbumTable.TABLE_NAME, AlbumTable.CONTENTID, contentId);
            if (albumTables != null && albumTables.size() != 0) {
                AlbumTable albumTable = albumTables.get(0);
                mImageList = (ArrayList<String>) albumTable.getAccessaryFileUrlList();
                return 1;
            }
            return 2;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer==1) {
                mBitmapUtils.display(mTop_imageView, mImageList.get(0));
            }
            initView();
        }
    }

    private void initView() {
        RecyclerView image_recyclerView = (RecyclerView) findViewById(R.id.image_recyclerView);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        ImageAdapter imageAdapter = new ImageAdapter(this, mImageList);
//        imageAdapter.setItemOnClickListener(new ImageAdapter.CustomItemOnClickListener() {
//            @Override
//            public void onItemClick(View v, int position) {
//                Intent intent = new Intent(ImageActivity.this, AlbumActivity.class);
//                if (mImageList != null) {
//                    //将所点击的图片放到第一个位置，浏览时首先打开这个图片，然后依次浏览其他的图片
//                    String tempStr = mImageList.get(position);
//                    mImageList.remove(position);
//                    mImageList.add(0, tempStr);
//                    intent.setAction("albumList");
//                    intent.putStringArrayListExtra("albumList", mImageList);
//                    startActivity(intent);
//                }
//            }
//        });
        image_recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(ImageActivity.this,image_recyclerView,
                new RecyclerItemClickListener.OnItemClickListener(){

                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(ImageActivity.this, AlbumActivity.class);
                        if (mImageList != null) {
                            //将所点击的图片放到第一个位置，浏览时首先打开这个图片，然后依次浏览其他的图片
                            String tempStr = mImageList.get(position);
                            mImageList.remove(position);
                            mImageList.add(0, tempStr);
                            intent.setAction("albumList");
                            intent.putStringArrayListExtra("albumList", mImageList);
                            startActivity(intent);
                        }

                    }
                    @Override
                    public void onItemLongClick(View view, int position) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ImageActivity.this);
                        builder.setTitle("删除图片");
                        builder.setMessage("删除的图片不可恢复，您确定删除？");
                        builder.setNegativeButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //// TODO: 2016/6/25 删除一条附件信息的方法
//                                new DeleteAlbumTask().execute();
                            }
                        });
                        builder.setPositiveButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                    }
                }));
        image_recyclerView.setLayoutManager(manager);
        image_recyclerView.setItemAnimator(new DefaultItemAnimator());
        image_recyclerView.setAdapter(imageAdapter);


    }

    private class DeleteAlbumTask extends AsyncTask<String,Integer,Integer> {

        private ProgressDialog mPd;

        @Override
        protected void onPreExecute() {
            mPd = ProgressDialog.show(ImageActivity.this, null, "正在删除...");
        }

        @Override
        protected Integer doInBackground(String... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            mPd.dismiss();
        }
    }
}
