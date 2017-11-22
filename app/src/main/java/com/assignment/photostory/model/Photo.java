package com.assignment.photostory.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.assignment.photostory.helper.PhotoHelper;

import java.io.File;

/**
 * Created by heeyan on 2017. 11. 21..
 */

public class Photo implements Parcelable{
    public File origin;
    public File thumb;

    public Photo(File origin) {
        this.origin = origin;
        this.thumb = PhotoHelper.saveThumbnail(origin, origin.getName() + "_thumbs");
    }

    public Photo(File origin, File thumb) {
        this.origin = origin;
        this.thumb = thumb;
    }

    //================================================================================
    // action logic for photo
    //================================================================================
    public void removePhoto() throws NullPointerException{
        origin.delete();
        thumb.delete();
    }




    //================================================================================
    // parcelable implement
    //================================================================================
    public Photo(Parcel in) {
        origin = (File) in.readSerializable();
        thumb = (File) in.readSerializable();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(origin);
        dest.writeSerializable(thumb);
    }
}
