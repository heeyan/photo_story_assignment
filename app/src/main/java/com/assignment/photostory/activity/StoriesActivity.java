package com.assignment.photostory.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.assignment.photostory.R;
import com.assignment.photostory.model.Stories;
import com.assignment.photostory.model.Story;
import com.assignment.photostory.util.DateUtil;
import com.assignment.photostory.viewmodel.activity.StoriesViewModel;
import com.assignment.photostory.viewmodel.activity.StoryViewModel;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class StoriesActivity extends BaseActivity {

    // viewmodel
    StoriesViewModel storiesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();

        storiesViewModel = new StoriesViewModel(new Stories());

        findViews();
        setViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        storiesViewModel.refreshStories();
    }

    // views
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private EditText searchEdit;
    private Button searchButton;
    private void findViews(){
        setContentView(R.layout.activity_stories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);

        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        searchEdit = findViewById(R.id.search_edit);
        searchButton = findViewById(R.id.search_button);
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

        compositeDisposable.add(storiesViewModel.getStoriesObservable().subscribe(new Consumer<List<Story>>() {
            @Override
            public void accept(List<Story> stories) throws Exception {
                setRecyclerAdapter(stories);
            }
        }));
    }








//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_stories, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    //================================================================================
    // binded view events...
    //================================================================================
    private void goStoryCamera(){
        startActivity(new Intent(this, StoryCameraActivity.class));
    }

    private void setRecyclerAdapter(List<Story> stories){
        recyclerView.setAdapter(new RecyclerAdapter(stories));
    }

    private void search(String query){
        storiesViewModel.search(query);
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



    //================================================================================
    // Adapter for recycler :: 임시 구현
    //================================================================================
    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
        final int TYPE_DATE_MONTH = 1;
        final int TYPE_STORY = 2;

        List<Item> items = new ArrayList<>();

        public RecyclerAdapter(List<Story> stories) {
            String dateMonth = "";
            Item lastDateMonthItem = null;
            int storyCount = 0;
            for(Story story : stories){
                String newDateMonth = DateUtil.parceToMonthString(story.updatedAt);
                if(!dateMonth.equals(newDateMonth)){
                    if(lastDateMonthItem != null){
                        lastDateMonthItem.dateMonth = lastDateMonthItem.dateMonth + " ("+storyCount+")";
                    }
                    dateMonth = newDateMonth;
                    lastDateMonthItem = new Item(dateMonth);
                    storyCount = 0;
                    items.add(lastDateMonthItem);
                }
                storyCount++;
                items.add(new Item(story));
            }
            if(lastDateMonthItem != null){
                lastDateMonthItem.dateMonth = lastDateMonthItem.dateMonth + " ("+storyCount+")";
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType==TYPE_DATE_MONTH){
                TextView dateMonth = new TextView(parent.getContext());
                int padding = (int)getResources().getDisplayMetrics().density * 4;
                dateMonth.setPadding(padding, padding, padding, padding);
                return new ViewHolder(dateMonth, viewType);
            }else{
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_story_recycler, parent, false);
                return new ViewHolder(v, viewType);
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            if(items.get(position).viewType==TYPE_DATE_MONTH){
                holder.dateMonth.setText(items.get(position).dateMonth);
            }else{
                final Story story = items.get(position).story;
                holder.storyPhoto.setImageBitmap(BitmapFactory.decodeFile(story.photos.get(0).thumb.toString()));
                holder.title.setText(story.title);
                holder.updateDate.setText(DateUtil.parceToDateTimeString(story.updatedAt));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goStory(new Story(story.getId()));
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        storiesViewModel.removeStory(story.getId());
                        Toast.makeText(getApplicationContext(), "story removed", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public int getItemViewType(int position) {
            return items.get(position).viewType;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView dateMonth;
            ImageView storyPhoto;
            TextView title;
            TextView updateDate;
            View itemView;

            public ViewHolder(View itemView, int viewType) {
                super(itemView);
                if(viewType==TYPE_DATE_MONTH){
                    dateMonth = (TextView) itemView;
                }else{
                    this.itemView = itemView;
                    storyPhoto = itemView.findViewById(R.id.story_photo);
                    title = itemView.findViewById(R.id.title);
                    updateDate = itemView.findViewById(R.id.updated_at);
                }
            }
        }

        class Item{
            int viewType;
            Story story;
            String dateMonth;

            public Item(String dateMonth) {
                this.dateMonth = dateMonth;
                this.viewType = TYPE_DATE_MONTH;
            }

            public Item(Story story) {
                this.story = story;
                this.viewType = TYPE_STORY;
            }
        }
    }

    private void goStory(Story story){
        Intent intent = new Intent(this, StoryActivity.class);
        intent.putExtra("mode", StoryViewModel.MODE.EDIT);
        intent.putExtra("story", story);
        startActivity(intent);
    }



}
