package com.assignment.photostory.viewmodel;

import android.support.annotation.NonNull;

import com.assignment.photostory.model.Photo;
import com.assignment.photostory.model.Story;

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by heeyan on 2017. 11. 23..
 */

public class StoryViewModel extends ViewModel {

    @NonNull
    public final Story story;

    public StoryViewModel(@NonNull Story story) {
        this.story = story;
    }

    public Observable<Date> getUpdatedAtObservable(){
        return Observable.just(story.updatedAt);
    }

    public Observable<String> getTitleObservable(){
        return Observable.just(story.title);
    }

    public Observable<String> getBodyObservable(){
        return Observable.just(story.body);
    }

    public Observable<List<Photo>> getPhotosObservable(){
        return Observable.just(story.photos);
    }
}
