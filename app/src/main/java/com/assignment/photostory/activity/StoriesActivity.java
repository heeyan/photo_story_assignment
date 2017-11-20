package com.assignment.photostory.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.assignment.photostory.R;
import com.assignment.photostory.helper.RealmHelper;
import com.assignment.photostory.realm.object.StoryObject;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class StoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestPermissions();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goStoryCamera();
            }
        });



        RealmHelper.transaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<StoryObject> results = realm.where(StoryObject.class).findAll().sort("id", Sort.ASCENDING);
                long newId;
                if(results.isEmpty()){
                    newId = 1;
                }else{
                    newId = results.last().id + 1;
                }

                final StoryObject storyObject = new StoryObject();
                storyObject.id = newId;
                storyObject.title = "test title " + SystemClock.currentThreadTimeMillis();
                storyObject.body = "test body :: " + SystemClock.currentThreadTimeMillis();

                realm.copyToRealm(storyObject);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("tttttt", "story :: " + RealmHelper.getRealm().where(StoryObject.class).findAll());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stories, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goStoryCamera(){
        startActivity(new Intent(this, StoryCameraActivity.class));
    }


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
