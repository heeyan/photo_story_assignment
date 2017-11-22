package com.assignment.photostory.adapter.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.assignment.photostory.PhotoStoryApplication;
import com.assignment.photostory.view.custom.CustomView;

import java.util.ArrayList;
import java.util.List;

/**
 * 커스텀뷰와 모델뷰로 이루어진 UI컴퍼넌트들을 특별한 아답터 제작없이 공용으로 사용할 수 있도록 하는것이 이 아답터의 목적이다.
 * 리싸이클러 뷰의 아이템이 되는 리스트의 인자들을 RecyclerModel(커스텀뷰.class, 뷰모델)의 형태로 선언하면
 * 해당 커스텀뷰가 모델에 있는 데이터를 갖춘 형태로 생성된다.
 *
 * viewType 갱신을 위해 update 마다 models참조를 O(n)만큼 하게된다.
 * 기능에 비해 많은 리소스를 차지하므로 추후 개선이 필요하다
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{
    private List<RecyclerModel> models;
    private List<Class<? extends CustomView>> customViews = new ArrayList<>();

    public RecyclerAdapter(List<RecyclerModel> models) {
        if (models == null) {
            throw new IllegalArgumentException("models must not be null");
        }
        this.models = models;
    }

    @Override
    public int getItemViewType(int position) {
        return models.get(position).getViewType();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        updateCustomViews();
        CustomView itemView = newViewHolderInstance(customViews.get(viewType));
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.bindViewHolderWith(models.get(position).getViewModel());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public CustomView newViewHolderInstance(Class<? extends CustomView> customViewClass){
        try{
            return customViewClass.getConstructor(Context.class).newInstance(PhotoStoryApplication.getContext());
        }catch (Exception e){
            return new CustomView(PhotoStoryApplication.getContext()) {};
        }
    }

    private void updateCustomViews(){
        for(RecyclerModel recyclerModel : models){
            if(recyclerModel.isTypeSet())
                continue;

            Class<? extends CustomView> customViewClass = recyclerModel.getCustomViewClass();
            if(!customViews.contains(customViewClass)){
                customViews.add(customViewClass);
            }
            recyclerModel.setViewType(customViews.indexOf(customViewClass));
        }
    }

    //이 아답터를 사용할 시엔 notifyDataSetChanged 대신 아래의 update를 사용할 것
    public void update(){
        updateCustomViews();
        notifyDataSetChanged();
    }
}
