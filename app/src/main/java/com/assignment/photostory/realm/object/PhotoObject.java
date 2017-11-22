package com.assignment.photostory.realm.object;

import com.assignment.photostory.model.Photo;

import io.realm.RealmObject;

/**
 * Created by heeyan on 2017. 11. 20..
 */

public class PhotoObject extends RealmObject {
    public String photoUrl;
    public String thumbUrl;

    public PhotoObject() {
    }

    public PhotoObject(Photo photo){
        this.photoUrl = photo.origin.toString();
        this.thumbUrl = photo.thumb.toString();
    }

    public PhotoObject(String photoUrl, String thumbUrl) {
        this.photoUrl = photoUrl;
        this.thumbUrl = thumbUrl;
    }
}
