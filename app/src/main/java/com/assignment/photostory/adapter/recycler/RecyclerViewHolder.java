package com.assignment.photostory.adapter.recycler;

import android.support.v7.widget.RecyclerView;

import com.assignment.photostory.view.custom.CustomView;
import com.assignment.photostory.viewmodel.ViewModel;

/**
 * Created by heeyan on 2015. 12. 8..
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder{
    protected CustomView customView;

    public RecyclerViewHolder(CustomView customView) {
        super(customView);
        this.customView = customView;
    }

    public void bindViewHolderWith(ViewModel viewModel){
        try{
            customView.setViewModel(viewModel);
        }catch (Exception e){}
    };
}
