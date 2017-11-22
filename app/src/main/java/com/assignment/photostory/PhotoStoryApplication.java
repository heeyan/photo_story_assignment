package com.assignment.photostory;

import android.app.Application;

import com.assignment.photostory.helper.RealmHelper;

/**
 * Created by heeyan on 2017. 11. 20..
 */

public class PhotoStoryApplication extends Application {

    public static PhotoStoryApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        RealmHelper.init(this);
    }


}
