package com.assignment.photostory.activity;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.Toast;

import com.assignment.photostory.R;
import com.assignment.photostory.helper.PhotoHelper;
import com.assignment.photostory.helper.RedirectHelper;
import com.assignment.photostory.model.Story;
import com.assignment.photostory.viewmodel.activity.StoryActivityViewModel;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.File;
import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class StoryCameraActivity extends BaseActivity implements SurfaceHolder.Callback {

    // viewmodel
    StoryActivityViewModel storyActivityViewModel;

    BehaviorSubject<byte[]> photoSubject = BehaviorSubject.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storyActivityViewModel = new StoryActivityViewModel(new Story(), StoryActivityViewModel.MODE.WRITE);

        findViews();
        setViews();
    }


    // views
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Button closeButton;
    Button shotButton;
    Button doneButton;
    private void findViews(){
        setContentView(R.layout.activity_story_camera);

        surfaceView = findViewById(R.id.surface_view);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        closeButton = findViewById(R.id.close);
        shotButton = findViewById(R.id.shot);
        doneButton = findViewById(R.id.done);
    }


    // binding views with viewmodel through RxJava...
    private void setViews(){
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
                                goStory();
                            }
                        }));

        compositeDisposable.add(
                photoSubject.subscribeOn(Schedulers.io())
                        .map(new Function<byte[], File>() {
                            @Override
                            public File apply(byte[] bytes) throws Exception {
                                return PhotoHelper.savePhotoToFile(bytes, String.format("ps_%d", System.currentTimeMillis()));
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<File>() {
                            @Override
                            public void accept(File file) throws Exception {
                                storyActivityViewModel.addPhoto(file);
                            }
                        }));

        compositeDisposable.add(storyActivityViewModel.getPhotoCountObservable().subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer count) throws Exception {
                setDoneButton(count);
            }
        }));
    }


    //================================================================================
    // binded view events...
    //================================================================================
    private void setDoneButton(Integer count){
        doneButton.setText(String.format("완료(%d)", count));
    }

    private void goStory(){
        if(storyActivityViewModel.isPhotsEmpty()){
            Toast.makeText(this, "사진 촬영을 먼저 해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        RedirectHelper.goStory(this, storyActivityViewModel.getStory(), storyActivityViewModel.mode);
        finish();
    }


    // <구현 선택 사항>
    // 사진 촬영 후 스토리 작성화면 이동시 List<File> 형태로 데이터를 건내주기 때문에
    // 현재 단계에서 액션이 취소 될 시에 파일로 저장해둔 이미지를 삭제 해야 한다.
    // 이미지를 파일로 저장해서 넘기도록 할 경우 앱 동작 상태에 따라
    // 최종적으로 스토리가 되지않는 이미지 파일들은 삭제 해주어야 한다.
    // List<Bitmap> 형태로 넘기면 이미지 파일 삭제 처리 이슈는 사라지지만
    // 메모리 사용에 있어서 비효율적일 수 있다.
    private void cancel(){
        storyActivityViewModel.cancelPhoto();
        finish();
    }

    @Override
    public void onBackPressed() {
        cancel();
    }



    //================================================================================
    // for camera and surfaceholder callback
    //================================================================================
    Camera camera;
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
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

    Camera.PictureCallback jpegCallback;
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
}



