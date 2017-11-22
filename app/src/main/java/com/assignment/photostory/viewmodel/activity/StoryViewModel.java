package com.assignment.photostory.viewmodel.activity;

import android.support.annotation.NonNull;

import com.assignment.photostory.model.Photo;
import com.assignment.photostory.model.Story;

import java.io.File;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by heeyan on 2017. 11. 21..
 */

public class StoryViewModel {
    public enum MODE{
        WRITE, EDIT;
    }

    @NonNull
    public final Story story;

    @NonNull
    public final MODE mode;

    private Subject<List<Photo>> photosSubject = BehaviorSubject.create();

    public StoryViewModel(@NonNull Story story, @NonNull MODE mode) {
        this.story = story;
        this.mode = mode;
    }

    public Observable<MODE> getModeObservable(){
        return Observable.just(mode);
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

    public Observable<Integer> getPhotoCountObservable(){
        return photosSubject.map(new Function<List<Photo>, Integer>() {
            @Override
            public Integer apply(List<Photo> photos) throws Exception {
                return new Integer(photos.size());
            }
        });
    }

    public void addPhoto(File photo){
        story.addPhoto(photo);
        photosSubject.onNext(story.photos);
    }

    public void cancelPhoto(){
        story.cancelStory();
    }

    public void storyDone(String title, String body){
        story.title = title;
        story.body = body;
        if(mode.equals(MODE.WRITE)){
            story.writeStory();
        }else if(mode.equals(MODE.EDIT)){
            story.editStory();
        }
    }

    public void storyCancel(){
        if(mode.equals(MODE.WRITE)){
            story.cancelStory();
        }else if(mode.equals(MODE.EDIT)){
            // do nothing
        }
    }
}
