package com.geekband.huzhouapp.nav;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.database.dto.DataOperation;
import com.database.pojo.AlbumTable;
import com.database.pojo.Document;
import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.application.MyApplication;
import com.geekband.huzhouapp.baseadapter.CommonAdapter;
import com.geekband.huzhouapp.baseadapter.ViewHolder;
import com.geekband.huzhouapp.utils.BitmapHelper;
import com.geekband.huzhouapp.utils.Constants;
import com.geekband.huzhouapp.utils.DataUtils;
import com.geekband.huzhouapp.utils.SelectPicPopupWindow;
import com.geekband.huzhouapp.utils.UriToPathUtils;
import com.geekband.huzhouapp.vo.AlbumInfo;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2016/5/16
 */
public class GalleryActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    private TextView mGallery_back_textBtn;
    private ImageButton mCreate_gallery_btn;
    private RelativeLayout mGallery_layout;
    private ImageButton mLocal_gallery_imageView;
    private ImageButton mUp_photo_imageView;
    private ImageButton mAnimation_imageView;
    private boolean isCreate;
    private ProgressBar mGallery_progress;
    private GridView mGallery_gridView;
    private SelectPicPopupWindow mPicPopupWindow;
    private Uri photoUri;
    /**
     * 使用照相机拍照获取图片
     */
    public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
    /**
     * 使用相册中的图片
     */
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
    /**
     * 获取到的图片路径
     */
    public String picPath = "";
    private ImageView mSelected_pic;
    private ScrollView mSelected_layout;
    private Button mUp_btn;
    private ArrayList<AlbumInfo> mAlbumInfoList;
    private ArrayList<String> mImageThumbUrls;

    private final String IMAGE_TYPE = "image/*";
    private final int IMAGE_CODE = 0;   //这里的IMAGE_CODE是自己任意定义的
    private BitmapUtils mBitmapUtils;
    private ProgressDialog mPd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        isCreate = true;
        //控件信息
        getWidget();
        //加载本地数据
        new LocalTask().execute();


    }

    @Override
    protected void onPause() {
        super.onPause();
        //加载网络数据
        new NetTask().execute();
    }

    private void initAlbum() {
        mBitmapUtils = BitmapHelper.getBitmapUtils(this, mGallery_gridView, 0, 0);
        mGallery_gridView.setAdapter(new CommonAdapter<AlbumInfo>(this,mAlbumInfoList,R.layout.item_album) {
            @Override
            public void convert(ViewHolder viewHolder, AlbumInfo item) {
                viewHolder.setText(R.id.item_albumName, item.getAlbumName());
                viewHolder.setText(R.id.item_albumCount, String.valueOf(item.getAlbumCount()));
                ImageView imageView = viewHolder.getView(R.id.item_albumImage);
                mBitmapUtils.display(imageView,item.getAlbumUrl());
            }
        });
        mGallery_gridView.setOnItemClickListener(this);
    }

    private void getWidget() {
        mGallery_back_textBtn = (TextView) findViewById(R.id.gallery_back_textBtn);
        mCreate_gallery_btn = (ImageButton) findViewById(R.id.create_gallery_btn);

        mGallery_layout = (RelativeLayout) findViewById(R.id.gallery_layout);
        mLocal_gallery_imageView = (ImageButton) findViewById(R.id.local_gallery_imageView);
        mUp_photo_imageView = (ImageButton) findViewById(R.id.up_photo_imageView);
        mAnimation_imageView = (ImageButton) findViewById(R.id.animation_imageView);

        mGallery_progress = (ProgressBar) findViewById(R.id.gallery_progress);
        mGallery_gridView = (GridView) findViewById(R.id.gallery_gridView);

        mSelected_layout = (ScrollView) findViewById(R.id.selected_layout);
        mSelected_pic = (ImageView) findViewById(R.id.selected_pic);
        mUp_btn = (Button) findViewById(R.id.up_btn);

        mGallery_back_textBtn.setOnClickListener(this);
        mCreate_gallery_btn.setOnClickListener(this);
        mLocal_gallery_imageView.setOnClickListener(this);
        mUp_photo_imageView.setOnClickListener(this);
        mAnimation_imageView.setOnClickListener(this);
        mUp_btn.setOnClickListener(this);
        mGallery_gridView.setOnItemClickListener(this);
        mGallery_gridView.setOnItemLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gallery_back_textBtn:
                //处理界面跳转
                this.finish();

                break;
            case R.id.create_gallery_btn:
                if (isCreate) {
                    mCreate_gallery_btn.setBackgroundResource(R.mipmap.create_cancle_btn);
                    mGallery_layout.setVisibility(View.VISIBLE);
                    isCreate = false;
                } else {
                    mCreate_gallery_btn.setBackgroundResource(R.mipmap.create_gallery_btn);
                    mGallery_layout.setVisibility(View.GONE);
                    mSelected_layout.setVisibility(View.GONE);
                    mGallery_gridView.setVisibility(View.VISIBLE);
                    new LocalTask().execute();
                    isCreate = true;
                }
                break;
            case R.id.local_gallery_imageView:
                Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                getAlbum.setType(IMAGE_TYPE);
                startActivity(getAlbum);
                break;
            case R.id.up_photo_imageView:

                mGallery_gridView.setVisibility(View.GONE);
                mSelected_layout.setVisibility(View.VISIBLE);

                mPicPopupWindow = new SelectPicPopupWindow(GalleryActivity.this, itemsOnClick);
                mPicPopupWindow.showAtLocation(findViewById(R.id.gallery_main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.animation_imageView:
                break;

            case R.id.up_btn:
                new UpTask().execute();
                break;
        }
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 隐藏弹出窗口
            mPicPopupWindow.dismiss();

            switch (v.getId()) {
                case R.id.takePhotoBtn:// 拍照
                    takePhoto();
                    break;
                case R.id.pickPhotoBtn:// 相册选择图片
                    pickPhoto();
                    break;
                case R.id.cancelBtn:// 取消
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 拍照获取图片
     */
    private void takePhoto() {
        // 执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            /***
             * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
             * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
             * 如果不使用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
             */
            ContentValues values = new ContentValues();
            photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        } else {
            Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {
//        Intent intent = new Intent();
//        // 如果要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
//使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片

        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
        getAlbum.setType(IMAGE_TYPE);
        startActivityForResult(getAlbum, IMAGE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {        //此处的 RESULT_OK 是系统自定义得一个常量
            return;
        }

        doPhoto(requestCode, data);
//        // 点击取消按钮
//        if (resultCode == RESULT_CANCELED) {
//            return;
//        }
//
//        // 可以使用同一个方法，这里分开写为了防止以后扩展不同的需求
//        switch (requestCode) {
//            case SELECT_PIC_BY_PICK_PHOTO:// 如果是直接从相册获取
//                doPhoto(requestCode, data);
//                break;
//            case SELECT_PIC_BY_TACK_PHOTO:// 如果是调用相机拍照时
//                doPhoto(requestCode, data);
//                break;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 选择图片后，获取图片的路径
     *
     * @param requestCode
     * @param data
     */
    private void doPhoto(int requestCode, Intent data) {

        Bitmap bitmap = null;
        //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
        ContentResolver resolver = getContentResolver();

        //此处的用于判断接收的Activity是不是你想要的那个
        if (requestCode == IMAGE_CODE) {

            try {
                Uri originalUri = data.getData();        //获得图片的uri
                bitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                //显得到bitmap图片
                mSelected_pic.setImageBitmap(bitmap);
                picPath = UriToPathUtils.getPath(this, originalUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, AlbumActivity.class);
        if (mImageThumbUrls != null) {
            //将所点击的图片放到第一个位置，浏览时首先打开这个图片，然后依次浏览其他的图片
            String tempStr = mImageThumbUrls.get(position);
            mImageThumbUrls.remove(position);
            mImageThumbUrls.add(0, tempStr);
            intent.setAction("albumList");
            intent.putStringArrayListExtra("albumList", mImageThumbUrls);
            this.startActivity(intent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = (TextView) view.findViewById(R.id.item_albumName);
        final String albumName = textView.getText().toString();
        showDeleteDialog(albumName);
        return true;
    }

    class LocalTask extends AsyncTask<String, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            mGallery_progress.setVisibility(View.VISIBLE);
            mGallery_gridView.setVisibility(View.GONE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                //相册信息
                mAlbumInfoList = (ArrayList<AlbumInfo>) MyApplication.sDbUtils.findAll(AlbumInfo.class);
                if (mAlbumInfoList != null) {
                    //封面图地址
                    mImageThumbUrls = new ArrayList<>();
                    for (AlbumInfo albumInfo : mAlbumInfoList) {
                        String albumUrl = albumInfo.getAlbumUrl();
                        mImageThumbUrls.add(albumUrl);
                    }

                    return 1;
                }
            } catch (DbException e) {
                e.printStackTrace();
            }

            return 2;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer == 1) {
                mGallery_progress.setVisibility(View.GONE);
                mGallery_gridView.setVisibility(View.VISIBLE);
                //初始化相册GridView
                initAlbum();
            }else {
                Toast.makeText(GalleryActivity.this,"暂无数据或者还未同步到服务器",Toast.LENGTH_SHORT).show();
            }
        }

    }

    class UpTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            mGallery_progress.setVisibility(View.VISIBLE);
            mPd = ProgressDialog.show(GalleryActivity.this, null, "正在上传...");
        }

        @Override
        protected Integer doInBackground(String... params) {

            String contentId = MyApplication.sSharedPreferences.getString(Constants.AUTO_LOGIN, null);
            AlbumTable albumTable = new AlbumTable();
            Date date = new Date();
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
            String str = df.format(date);

            albumTable.putField(AlbumTable.FIELD_NAME, str);
            albumTable.putField(AlbumTable.FIELD_USERID, contentId);
            DataOperation.insertOrUpdateTable(albumTable, new Document(picPath));

            DataUtils.saveAlbum(contentId);

            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            mGallery_progress.setVisibility(View.GONE);
            mSelected_layout.setVisibility(View.GONE);
            mPd.dismiss();
            new NetTask().execute();
        }
    }


    class NetTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            String contentId = MyApplication.sSharedPreferences.getString(Constants.AUTO_LOGIN, null);
            DataUtils.saveAlbum(contentId);
            return null;
        }
    }

    public void showDeleteDialog(final String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除图片");
        builder.setMessage("删除的图片不可恢复，您确认删除？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new DeleteAlbumTask().execute(name);
                    }
                }

        );
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //取消删除

                    }
                }
        );

        builder.create().show();
    }

    class DeleteAlbumTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            AlbumTable selectAlbum = null;
            String contentId = MyApplication.sSharedPreferences.getString(Constants.AUTO_LOGIN, null);
            //noinspection unchecked
            ArrayList<AlbumTable> albumTables = (ArrayList<AlbumTable>) DataOperation.queryTable(AlbumTable.TABLE_NAME, AlbumTable.FIELD_USERID,contentId);
            if (albumTables != null) {
                for (AlbumTable albumTable : albumTables) {
                    if (albumTable.getField(AlbumTable.FIELD_NAME).equals(params[0])) {
                        selectAlbum = albumTable;
                        break;
                    }
                }
            }
            DataOperation.deleteTable(selectAlbum);
            try {
                MyApplication.sDbUtils.delete(AlbumInfo.class, WhereBuilder.b("albumName", "=", params[0]));
            } catch (DbException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            //刷新界面
            new LocalTask().execute();
        }

    }

}