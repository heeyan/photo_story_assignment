package com.assignment.photostory.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.assignment.photostory.R;
import com.assignment.photostory.helper.RedirectHelper;
import com.assignment.photostory.model.Photo;
import com.assignment.photostory.model.Story;
import com.assignment.photostory.util.DateUtil;
import com.assignment.photostory.viewmodel.activity.StoryActivityViewModel;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Date;
import java.util.List;

import io.reactivex.functions.Consumer;

public class StoryActivity extends BaseActivity {

    // viewmodel
    StoryActivityViewModel storyActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get story for writing or editing
        Story story = RedirectHelper.parceStory(getIntent());
        StoryActivityViewModel.MODE mode = RedirectHelper.parceStoryActivityMode(getIntent());

        if(story == null){
            finish();
            return;
        }

        // creating viewmodel
        storyActivityViewModel = new StoryActivityViewModel(story, mode);

        findViews();
        setViews();
    }

    // views
    private TextView photoCount;
    private LinearLayout photoLinear;
    private TextView updatedAt;
    private EditText title;
    private EditText body;
    private Button done;
    private View photoDetailLayer;
    private ImageView photoDetail;
    private Button closePhotoDetail;
    private void findViews(){
        setContentView(R.layout.activity_story);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        photoCount = findViewById(R.id.photo_count);
        photoLinear = findViewById(R.id.photo_linear);
        updatedAt = findViewById(R.id.updated_at);
        title = findViewById(R.id.title);
        body = findViewById(R.id.body);
        done = findViewById(R.id.done);
        photoDetailLayer = findViewById(R.id.photo_detail_layer);
        photoDetail = findViewById(R.id.photo_detail);
        closePhotoDetail = findViewById(R.id.close_photo_detail);
    }

    // binding views with viewmodel through RxJava...
    private void setViews(){
        compositeDisposable.add(storyActivityViewModel.getModeObservable().subscribe(new Consumer<StoryActivityViewModel.MODE>() {
            @Override
            public void accept(StoryActivityViewModel.MODE mode) throws Exception {
                setDoneButton(mode);
            }
        }));

        compositeDisposable.add(storyActivityViewModel.getPhotosObservable().subscribe(new Consumer<List<Photo>>() {
            @Override
            public void accept(List<Photo> photos) throws Exception {
                setPhotoLinear(photos);
            }
        }));

        compositeDisposable.add(storyActivityViewModel.getUpdatedAtObservable().subscribe(new Consumer<Date>() {
            @Override
            public void accept(Date updatedAt) throws Exception {
                setUpdatedAt(updatedAt);
            }
        }));

        compositeDisposable.add(storyActivityViewModel.getTitleObservable().subscribe(new Consumer<String>() {
            @Override
            public void accept(String title) throws Exception {
                setTitle(title);
            }
        }));

        compositeDisposable.add(storyActivityViewModel.getBodyObservable().subscribe(new Consumer<String>() {
            @Override
            public void accept(String body) throws Exception {
                setBody(body);
            }
        }));

        compositeDisposable.add(RxView.clicks(done).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                storyDone();
            }
        }));

        compositeDisposable.add(RxView.clicks(closePhotoDetail).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                closePhotoDetail();
            }
        }));
    }


    //================================================================================
    // binded view events...
    //================================================================================
    private  void setDoneButton(StoryActivityViewModel.MODE mode){
        if(mode.equals(StoryActivityViewModel.MODE.WRITE)){
            done.setText("저장");
        }else if(mode.equals(StoryActivityViewModel.MODE.EDIT)){
            done.setText("수정");
        }
    }

    private void setPhotoLinear(List<Photo> photos){
        photoCount.setText(photos.size() + "장의 사진");

        for(final Photo photo : photos){
            ImageView thumbImageView = new ImageView(this);
            int size = (int)getResources().getDisplayMetrics().density * 120;
            thumbImageView.setLayoutParams(new LinearLayout.LayoutParams(size, size));
            thumbImageView.setImageBitmap(BitmapFactory.decodeFile(photo.thumb.toString()));
            thumbImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    photoDetail.setImageBitmap(BitmapFactory.decodeFile(photo.origin.toString()));
                    photoDetailLayer.setVisibility(View.VISIBLE);
                }
            });
            photoLinear.addView(thumbImageView);
        }
    }

    private void setUpdatedAt(Date updatedAt){
        if(storyActivityViewModel.mode.equals(StoryActivityViewModel.MODE.WRITE)){
            this.updatedAt.setText(DateUtil.parceToDateTimeString(updatedAt));
        }else if(storyActivityViewModel.mode.equals(StoryActivityViewModel.MODE.EDIT)){
            this.updatedAt.setText("마지막 수정 : " + DateUtil.parceToDateTimeString(updatedAt));
        }
    }

    private void setTitle(String title){
        this.title.setText(title);
    }

    private void setBody(String body){
        this.body.setText(body);
    }

    private void storyDone(){
        storyActivityViewModel.storyDone(title.getText().toString(), body.getText().toString());
        finish();
    }

    private void storyCancel(){
        storyActivityViewModel.storyCancel();
        finish();
    }

    private void closePhotoDetail(){
        photoDetailLayer.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        storyCancel();
    }
}
