package com.assignment.photostory.model;


import java.io.File;

import io.reactivex.Observable;

/**
 * Created by heeyan on 2017. 11. 21..
 */

public class Photo {
    public File photo;
    public File thumb;

    public Photo(File photo, File thumb) {
        this.photo = photo;
        this.thumb = thumb;
    }

    public Observable<File> getPhotoObservable(){
        return Observable.just(photo);
    }

    public Observable<File> getThumbObservable(){
        return Observable.just(thumb);
    }
}
