package com.assignment.photostory.viewmodel.activity;

import android.support.annotation.NonNull;

import com.assignment.photostory.model.Photo;
import com.assignment.photostory.model.Story;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by heeyan on 2017. 11. 21..
 */

public class StoryViewModel {

    @NonNull
    private final Story story;

    public StoryViewModel(@NonNull Story story) {
        this.story = story;
    }

    public Observable<Integer> getPhotoCountObservable(){
        return story.getPhotosObservable().flatMap(new Function<List<Photo>, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(List<Photo> photos) throws Exception {
                return Observable.just(new Integer(photos.size()));
            }
        });
    }

    public Observable<List<Photo>> getPhotosObservable(){
        return story.getPhotosObservable().flatMap(new Function<List<Photo>, ObservableSource<List<Photo>>>() {
            @Override
            public ObservableSource<List<Photo>> apply(List<Photo> photos) throws Exception {
                return Observable.just(photos);
            }
        });
    }
}
