package com.assignment.photostory.view.custom;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.assignment.photostory.R;
import com.assignment.photostory.util.DateUtil;
import com.assignment.photostory.viewmodel.ViewModel;
import com.assignment.photostory.viewmodel.view.StoryItemViewModel;

/**
 * Created by heeyan on 2017. 11. 23..
 */

public class StoryItemCustomView extends CustomViewMatchParentWidth {
    TextView dateMonth;
    View clickArea;
    ImageView storyPhoto;
    TextView title;
    TextView updateDate;

    public StoryItemCustomView(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.item_story_recycler, null);


        dateMonth = layout.findViewById(R.id.date_month);
        clickArea = layout.findViewById(R.id.click_area);
        storyPhoto = layout.findViewById(R.id.story_photo);
        title = layout.findViewById(R.id.title);
        updateDate = layout.findViewById(R.id.updated_at);

        addView(layout);
    }

    @Override
    public void setViewModel(ViewModel viewModel) {
        super.setViewModel(viewModel);
        final StoryItemViewModel storyItemViewModel = (StoryItemViewModel)viewModel;

        if(storyItemViewModel.dateMonth != null){
            dateMonth.setText(storyItemViewModel.dateMonth);
            dateMonth.setVisibility(VISIBLE);
        }else{
            dateMonth.setVisibility(GONE);
        }
        storyPhoto.setImageBitmap(BitmapFactory.decodeFile(storyItemViewModel.story.photos.get(0).thumb.toString()));
        title.setText(storyItemViewModel.story.title);
        updateDate.setText(DateUtil.parceToDateTimeString(storyItemViewModel.story.updatedAt));

        clickArea.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                storyItemViewModel.itemClicked();
            }
        });

        clickArea.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                storyItemViewModel.itemLongClicked();
                return true;
            }
        });
    }
}
