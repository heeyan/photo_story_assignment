package com.assignment.photostory.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by heeyan on 2017. 11. 20..
 */

public class BitmapUtil {
    private static final String DIRECTORY = "!photostory";
    public static File savePhotoToFile(byte[] data, String fileName) {
        File file = makePhotoFile(fileName);

        try {
            FileOutputStream out = new FileOutputStream(file);
            BitmapUtil.rotateImage(BitmapFactory.decodeByteArray(data, 0 , data.length), 90)
                    .compress(Bitmap.CompressFormat.JPEG, 60, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    public static File saveThumbnail(File originalFile, String fileName) {
        File file = makePhotoFile(fileName);

        try {
            FileOutputStream out = new FileOutputStream(file);
            ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(originalFile.toString()), 128, 128)
                    .compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    public static File makePhotoFile(String fileName){
        File direct = new File(Environment.getExternalStorageDirectory() + "/" + DIRECTORY);

        if (!direct.exists()) {
            File directory = new File("/sdcard/"+ DIRECTORY +"/");
            directory.mkdirs();
        }

        File file = new File(new File("/sdcard/"+ DIRECTORY +"/"), fileName + ".jpeg");
        if (file.exists()) {
            file.delete();
        }

        return file;
    }

    public static Bitmap rotateImage(Bitmap bitmap, int rotate){
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

}
