package com.assignment.photostory.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;

import com.assignment.photostory.PhotoStoryApplication;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by heeyan on 2017. 11. 20..
 */

public class PhotoHelper {
    private static final String DIRECTORY = ".photostory";
    public static File savePhotoToFile(byte[] data, String fileName) {
        File file = new File(PhotoStoryApplication.getContext().getFilesDir(), fileName + ".jpeg");
        try {
            FileOutputStream out = new FileOutputStream(file);
            rotateImage(BitmapFactory.decodeByteArray(data, 0 , data.length), 90)
                    .compress(Bitmap.CompressFormat.JPEG, 60, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File saveThumbnail(File originalFile, String fileName) {
        File file = new File(PhotoStoryApplication.getContext().getFilesDir(), fileName + ".jpeg");
        try {
            FileOutputStream out = new FileOutputStream(file);
            ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(originalFile.toString()), 256, 256)
                    .compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
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
