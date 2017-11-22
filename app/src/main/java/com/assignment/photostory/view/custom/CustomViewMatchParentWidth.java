package com.assignment.photostory.view.custom;

import android.content.Context;
import android.view.ViewGroup;

public class CustomViewMatchParentWidth extends CustomView {
    public CustomViewMatchParentWidth(Context context) {
        super(context);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }
}
