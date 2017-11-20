package com.assignment.photostory.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.assignment.photostory.R;
import com.assignment.photostory.model.Photo;
import com.assignment.photostory.model.Story;
import com.assignment.photostory.util.BitmapUtil;
import com.assignment.photostory.viewmodel.activity.StoryViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class StoryActivity extends BaseActivity {

    StoryViewModel storyViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_write);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String mode = getIntent().getStringExtra("mode");
        if(mode.equals("write")){
            Story story = new Story();
            for(File photo : (ArrayList<File>)getIntent().getSerializableExtra("sending_photos")){
                File thumb = BitmapUtil.saveThumbnail(photo, photo.getName() + "_thumb");
                story.photos.add(new Photo(photo, thumb));
            }

            storyViewModel = new StoryViewModel(story);
        }else if(mode.equals("show")){


        }else{
            finish();
            return;
        }

        findViews();
        setViews();
    }

    private TextView photoCount;
    private LinearLayout photoLinear;
    private EditText title;
    private EditText body;
    private void findViews(){
        photoCount = findViewById(R.id.photo_count);
        photoLinear = findViewById(R.id.photo_linear);
        title = findViewById(R.id.title);
        body = findViewById(R.id.body);
    }

    private void setViews(){
        compositeDisposable.add(storyViewModel.getPhotoCountObservable().subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                setPhotoCount(integer);
            }
        }));

        compositeDisposable.add(storyViewModel.getPhotosObservable().subscribe(new Consumer<List<Photo>>() {
            @Override
            public void accept(List<Photo> photos) throws Exception {
                setPhotoLinear(photos);
            }
        }));
    }

    private void setPhotoCount(Integer count){
        photoCount.setText(count + "장의 사진");
    }

    private void setPhotoLinear(List<Photo> photos){
        for(Photo photo : photos){
            ImageView thumbImageView = new ImageView(this);
            int size = (int)getResources().getDisplayMetrics().density * 120;
            thumbImageView.setLayoutParams(new LinearLayout.LayoutParams(size, size));
            thumbImageView.setImageBitmap(BitmapFactory.decodeFile(photo.thumb.toString()));
            photoLinear.addView(thumbImageView);
        }
    }

}
