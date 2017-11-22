package com.assignment.photostory.adapter.recycler;

import com.assignment.photostory.view.custom.CustomView;
import com.assignment.photostory.viewmodel.ViewModel;

/**
 * Created by heeyan on 2015. 12. 8..
 */
public class RecyclerModel {
    private final Class<? extends CustomView> customViewClass;
    private final ViewModel viewModel;

    private int viewType;
    private boolean isTypeSet = false;

    public RecyclerModel(Class<? extends CustomView> customViewClass, ViewModel viewModel) {
        this.customViewClass = customViewClass;
        this.viewModel = viewModel;
    }

    public Class<? extends CustomView> getCustomViewClass() {
        return customViewClass;
    }

    public ViewModel getViewModel() {
        return viewModel;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
        isTypeSet = true;
    }

    public boolean isTypeSet() {
        return isTypeSet;
    }
}
