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
    protected final Story story;

    public StoryViewModel(@NonNull Story story) {
        this.story = story;
    }

    public Observable<Date> getUpdatedAtObservable(){
        return Observable.just(story.getUpdatedAt());
    }

    public Observable<String> getTitleObservable(){
        return Observable.just(story.getTitle());
    }

    public Observable<String> getBodyObservable(){
        return Observable.just(story.getBody());
    }

    public Observable<List<Photo>> getPhotosObservable(){
        return Observable.just(story.getPhotos());
    }

    @NonNull
    public Story getStory() {
        return story;
    }

    public String getThumbsPath(){
        if(story.getPhotos().isEmpty()){
            return "";
        }else{
            return story.getPhotos().get(0).thumb.toString();
        }
    }

    public Date getUpdatedAt(){
        return story.getUpdatedAt();
    }

    public String getTitle(){
        return story.getTitle();
    }

    public String getBody(){
        return story.getBody();
    }

    public List<Photo> getPhotos(){
        return story.getPhotos();
    }
}
