package com.assignment.photostory.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.assignment.photostory.R;
import com.assignment.photostory.adapter.recycler.RecyclerAdapter;
import com.assignment.photostory.adapter.recycler.RecyclerModel;
import com.assignment.photostory.helper.RedirectHelper;
import com.assignment.photostory.model.Stories;
import com.assignment.photostory.model.Story;
import com.assignment.photostory.util.DateUtil;
import com.assignment.photostory.view.custom.StoryItemCustomView;
import com.assignment.photostory.viewmodel.activity.StoriesActivityViewModel;
import com.assignment.photostory.viewmodel.activity.StoryActivityViewModel;
import com.assignment.photostory.viewmodel.view.StoryItemViewModel;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class StoriesActivity extends BaseActivity {

    // viewmodel
    StoriesActivityViewModel storiesActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();

        storiesActivityViewModel = new StoriesActivityViewModel(new Stories());

        findViews();
        setViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        storiesActivityViewModel.refreshStories();
    }

    // views
    private FloatingActionButton fab;
    private EditText searchEdit;
    private Button searchButton;
    private void findViews(){
        setContentView(R.layout.activity_stories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initRecycler();

        fab = findViewById(R.id.fab);
        searchEdit = findViewById(R.id.search_edit);
        searchButton = findViewById(R.id.search_button);
    }

    // init for recyclerview
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private List<RecyclerModel> recyclerModels;
    private void initRecycler(){
        recyclerView = findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerModels = new ArrayList<>();
        recyclerAdapter = new RecyclerAdapter(recyclerModels);

        recyclerView.setAdapter(recyclerAdapter);
    }

    // binding views with viewmodel through RxJava...
    private void setViews(){
        compositeDisposable.add(RxView.clicks(fab).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                goStoryCamera();
            }
        }));

        compositeDisposable.add(RxView.clicks(searchButton).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                search(searchEdit.getText().toString());
            }
        }));

        compositeDisposable.add(storiesActivityViewModel.getStoriesObservable().subscribe(new Consumer<List<Story>>() {
            @Override
            public void accept(List<Story> stories) throws Exception {
                updateRecycler(stories);
            }
        }));
    }

    //================================================================================
    // binded view events...
    //================================================================================
    private void goStoryCamera(){
        RedirectHelper.goStoryCamera(this);
    }

    private void updateRecycler(List<Story> stories){
        recyclerModels.clear();
        List<RecyclerModel> monthStoryModels = new ArrayList<>();
        String prevDateMonth = "";
        for(Story story : stories){
            String newDateMonth = DateUtil.parceToMonthString(story.updatedAt);
            if(!prevDateMonth.equals(newDateMonth)){
                if(!monthStoryModels.isEmpty()){
                    ((StoryItemViewModel)monthStoryModels.get(0).getViewModel()).dateMonth = prevDateMonth + " (" + monthStoryModels.size() + ")";
                }
                recyclerModels.addAll(monthStoryModels);
                monthStoryModels.clear();
            }

            final StoryItemViewModel storyItemViewModel = new StoryItemViewModel(story);
            compositeDisposable.add(storyItemViewModel.getEventObservable().subscribe(new Consumer<StoryItemViewModel.EVENT>() {
                @Override
                public void accept(StoryItemViewModel.EVENT event) throws Exception {
                    if(event.equals(StoryItemViewModel.EVENT.CLICK)){
                        goStory(storyItemViewModel.story);
                    }else if(event.equals(StoryItemViewModel.EVENT.LONG_CLICK)){
                        storiesActivityViewModel.refreshStories();
                    }
                }
            }));
            monthStoryModels.add(new RecyclerModel(StoryItemCustomView.class, storyItemViewModel));
            prevDateMonth = newDateMonth;
        }
        if(!monthStoryModels.isEmpty()){
            ((StoryItemViewModel)monthStoryModels.get(0).getViewModel()).dateMonth = prevDateMonth + " (" + monthStoryModels.size() + ")";
        }
        recyclerModels.addAll(monthStoryModels);
        recyclerAdapter.update();
    }

    private void goStory(Story story){
        RedirectHelper.goStory(this, story, StoryActivityViewModel.MODE.EDIT);
    }

    private void search(String query){
        storiesActivityViewModel.search(query);
        hideKeyboard();
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    //================================================================================
    // for permission request version >= Android.M
    //================================================================================
    private static final int REQUEST_PERMISSION = 100;
    private void requestPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] PERMISSIONS = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            };

            for (String permission : PERMISSIONS) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(PERMISSIONS, REQUEST_PERMISSION);
                    return;
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                boolean permission = true;

                for(int result : grantResults){
                    if(result != PackageManager.PERMISSION_GRANTED){
                        permission = false;
                        break;
                    }
                }

                if(!permission){
                    finish();
                }
                break;
            default:
                break;
        }
    }

}
