package com.assignment.photostory.view.custom;

import android.content.Context;
import android.widget.FrameLayout;

import com.assignment.photostory.viewmodel.ViewModel;

public class CustomView extends FrameLayout{
    protected ViewModel viewModel;

    public CustomView(Context context) {
        super(context);
    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }
}
