package com.geekband.huzhouapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FileUtil {


    /**
     * 将Bitmap 图片保存到本地路径，并返回路径
     *  mType    资源类型，参照  MultimediaContentType 枚举，根据此类型，保存时可自动归类
     * @param c
     * @param fileName 文件名称
     * @param bitmap   图片
     * @return
     */
    public static String saveFile(Context c, String fileName, Bitmap bitmap) {
        return saveFile(c, "", fileName, bitmap);
    }

    public static String saveFile(Context c, String filePath, String fileName, Bitmap bitmap) {
        byte[] bytes = bitmapToBytes(bitmap);
        return saveFile(c, filePath, fileName, bytes);
    }

    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static String saveFile(Context c, String filePath, String fileName, byte[] bytes) {
        String fileFullName = "";
        FileOutputStream fos = null;
        String dateFolder = new SimpleDateFormat("yyyyMMdd", Locale.CHINA)
                .format(new Date());
        try {
            String suffix = "";
            if (filePath == null || filePath.trim().length() == 0) {
                filePath = Environment.getExternalStorageDirectory() + "/HZGA/" + dateFolder + "/";
            }
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File fullFile = new File(filePath, fileName + suffix);
            fileFullName = fullFile.getPath();
            fos = new FileOutputStream(new File(filePath, fileName + suffix));
            fos.write(bytes);
        } catch (Exception e) {
            fileFullName = "";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    fileFullName = "";
                }
            }
        }
        return fileFullName;
    }

    /**
     * 获取本地图片路径
     *
     * @param context
     * @return
     */
    public static ArrayList<String> getLocalImagePath(Context context) {
        ArrayList<String> imagePathList = new ArrayList<>();
        String[] images = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATA
        };
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, images, null, null, null
        );
        if (cursor != null && cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
//                System.out.println("这是测试分割线：");
//                System.out.println("图片ID"+cursor.getString(0)); // 图片ID
//                System.out.println("图片文件名"+cursor.getString(1)); // 图片文件名
//                System.out.println(cursor.getString(2)); // 图片绝对路径
                String path = cursor.getString(2);
                if (!path.contains("/storage/emulated/0/Pictures/Screenshots/img")){
                    imagePathList.add(path);
                }
            }
            cursor.close();
        }
        return imagePathList;
    }

    public static File saveFile(String typeFile,String fileName){
        String  filePath = Environment.getExternalStorageDirectory()+"/HZGA/"+typeFile+"/";
        File file = new File(filePath);
        if (!file.exists()){
            file.mkdirs();
        }

        File fullFile = new File(file,fileName);
        if (fullFile.exists()){
            fullFile.delete();
        }
        return fullFile;
    }

}
