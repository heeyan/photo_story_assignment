package com.assignment.photostory.activity;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.Toast;

import com.assignment.photostory.R;
import com.assignment.photostory.util.BitmapUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class StoryCameraActivity extends BaseActivity implements SurfaceHolder.Callback {

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Camera.PictureCallback jpegCallback;

    Button closeButton;
    Button shotButton;
    Button doneButton;

    BehaviorSubject<byte[]> photoSubject = BehaviorSubject.create();
    ArrayList<File> sendingPhotos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_camera);

        surfaceView = findViewById(R.id.surface_view);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        closeButton = findViewById(R.id.close);
        shotButton = findViewById(R.id.shot);
        doneButton = findViewById(R.id.done);

        compositeDisposable.add(
                RxView.clicks(closeButton)
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                cancel();
                            }
                        }));

        compositeDisposable.add(
                RxView.clicks(shotButton)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            captureImage();
                        }
                    }));

        compositeDisposable.add(
                RxView.clicks(doneButton)
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                goStoryWrite();
                            }
                        }));

        compositeDisposable.add(
                photoSubject.subscribeOn(Schedulers.io())
                        .map(new Function<byte[], File>() {
                            @Override
                            public File apply(byte[] bytes) throws Exception {
                                return BitmapUtil.savePhotoToFile(bytes, String.format("ps_%d", System.currentTimeMillis()));
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<File>() {
                            @Override
                            public void accept(File file) throws Exception {
                                sendingPhotos.add(file);
                                doneButton.setText(String.format("완료(%d)", sendingPhotos.size()));
                            }
                        }));
    }

    public void captureImage() throws IOException {
        if(jpegCallback == null){
            jpegCallback = new Camera.PictureCallback() {
                public void onPictureTaken(byte[] data, Camera camera) {
                    photoSubject.onNext(data);
                }
            };
        }
        camera.takePicture(null, null, jpegCallback);
    }

    private void goStoryWrite(){
        if(sendingPhotos.isEmpty()){
            Toast.makeText(this, "사진 촬영을 먼저 해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, StoryActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("sending_photos", sendingPhotos);
//        intent.putExtras(bundle);
        intent.putExtra("mode", "write");
        intent.putExtra("sending_photos", sendingPhotos);
        startActivity(intent);
        finish();
    }

    private void cancel(){
        for(File file : sendingPhotos){
            file.delete();
        }

        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cancel();
    }

    private void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        } catch (Exception e) {}

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {}
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        refreshCamera();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open();
            camera.setDisplayOrientation(90);
        } catch (RuntimeException e) {
            return;
        }

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {}
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if(camera != null){
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }
}



