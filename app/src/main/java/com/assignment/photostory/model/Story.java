package com.assignment.photostory.model;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by heeyan on 2017. 11. 21..
 */

public class Story {
    public long id;
    public String title;
    public String body;
    public List<Photo> photos;

    public Story() {
        photos = new ArrayList<>();
    }

    public Story(long id) {
        this.id = id;
    }

    public Observable<String> getTitleObservable(){
        return Observable.just(title);
    }

    public Observable<String> getBodyObservable(){
        return Observable.just(body);
    }

    public Observable<List<Photo>> getPhotosObservable(){
        return Observable.just(photos);
    }
}
