package com.geekband.huzhouapp.nav;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.database.dto.DataOperation;
import com.database.pojo.AlbumTable;
import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.baseadapter.CommonAdapter;
import com.geekband.huzhouapp.baseadapter.ViewHolder;
import com.geekband.huzhouapp.utils.BitmapHelper;
import com.geekband.huzhouapp.utils.Constants;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/24
 */
public class ImageActivity extends Activity implements AdapterView.OnItemClickListener {
    private ArrayList<String> mImageList;
    // private ImageView mTop_imageView;
    private BitmapUtils mBitmapUtils;
    private Intent mIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iamge);
        mImageList = new ArrayList<>();
        //mTop_imageView = (ImageView) findViewById(R.id.top_imageView);
        mBitmapUtils = BitmapHelper.getBitmapUtils(this, null, 0, 0);
        new LoadImageTask().execute();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent mIntent = new Intent(ImageActivity.this, AlbumActivity.class);
        if (mImageList != null) {
            mIntent.setAction("albumList");
            mIntent.putStringArrayListExtra("albumList", mImageList);
            mIntent.putExtra("position", position);
            startActivity(mIntent);
        }
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
            if (integer == 1 && mImageList.size() != 0) {
                //mBitmapUtils.display(mTop_imageView, mImageList.get(0));
            }
            initView();
        }
    }

    private void initView() {
 //       RecyclerView image_recyclerView = (RecyclerView) findViewById(R.id.image_recyclerView);
//        GridLayoutManager manager = new GridLayoutManager(this, 4);
//        ImageAdapter imageAdapter = new ImageAdapter(this, mImageList);
//
//        image_recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(ImageActivity.this,image_recyclerView,
//                new RecyclerItemClickListener.OnItemClickListener(){
//
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        Intent intent = new Intent(ImageActivity.this, AlbumActivity.class);
//                        if (mImageList != null) {
//                            intent.setAction("albumList");
//                            intent.putStringArrayListExtra("albumList", mImageList);
//                            intent.putExtra("position",position);
//                            startActivity(intent);
//                        }
//
//                    }
//                    @Override
//                    public void onItemLongClick(View view, int position) {
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(ImageActivity.this);
//                        builder.setTitle("删除图片");
//                        builder.setMessage("删除的图片不可恢复，您确定删除？");
//                        builder.setNegativeButton("是", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //// TODO: 2016/6/25 删除一条附件信息的方法
////                                new DeleteAlbumTask().execute();
//                            }
//                        });
//                        builder.setPositiveButton("否", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        });
//
//                        builder.create().show();
//
//                    }
//                }));
//        image_recyclerView.setLayoutManager(manager);
//        //image_recyclerView.setItemAnimator(new DefaultItemAnimator());
//        image_recyclerView.setAdapter(imageAdapter);


        GridView gridView_camera = (GridView) findViewById(R.id.gridView_image);
        mBitmapUtils = BitmapHelper.getBitmapUtils(this, gridView_camera, 0, 0);
        gridView_camera.setAdapter(new CommonAdapter<String>(this, mImageList, R.layout.item_camera_list) {
            @Override
            public void convert(ViewHolder viewHolder, String item) {
                mBitmapUtils.display(viewHolder.getView(R.id.imageView_camera), item);
            }
        });
        gridView_camera.setOnItemClickListener(this);

    }

    private class DeleteAlbumTask extends AsyncTask<String, Integer, Integer> {

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
