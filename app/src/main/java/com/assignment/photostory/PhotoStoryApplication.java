package com.assignment.photostory;

import android.app.Application;
import android.content.Context;

import com.assignment.photostory.helper.RealmHelper;

/**
 * Created by heeyan on 2017. 11. 20..
 */

public class PhotoStoryApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        RealmHelper.init(this);
    }

    public static Context getContext() {
        return context;
    }
}
