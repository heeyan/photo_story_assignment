package com.assignment.photostory.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.assignment.photostory.helper.PhotoHelper;
import com.assignment.photostory.helper.RealmHelper;
import com.assignment.photostory.realm.object.PhotoObject;
import com.assignment.photostory.realm.object.StoryObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by heeyan on 2017. 11. 21..
 */

public class Story implements Parcelable{
    private long id;
    public Date updatedAt;
    public String title;
    public String body;
    public List<Photo> photos;

    public Story() {
        id = -1;
        updatedAt = Calendar.getInstance().getTime();
        title = "";
        body = "";
        photos = new ArrayList<>();
    }

    public Story(long id) {
        this.id = id;
        StoryObject storyObject = RealmHelper.getRealm().where(StoryObject.class).equalTo("id", id).findFirst();
        this.updatedAt = storyObject.updatedAt;
        this.title = storyObject.title;
        this.body = storyObject.body;
        photos = new ArrayList<>();
        for(PhotoObject photoObject : storyObject.photos){
            photos.add(new Photo(new File(photoObject.photoUrl), new File(photoObject.thumbUrl)));
        }
    }

    public long getId() {
        return id;
    }

    public void addPhoto(File photo){
        photos.add(new Photo(photo, PhotoHelper.saveThumbnail(photo, photo.getName()+"_thumb")));
    }

    public void writeStory(){
        if(id>0){return;}

        RealmHelper.transaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<StoryObject> results = realm.where(StoryObject.class).findAll().sort("id", Sort.ASCENDING);
                long newId;
                if (results.isEmpty()) {
                    newId = 1;
                } else {
                    newId = results.last().id + 1;
                }

                StoryObject storyObject = realm.createObject(StoryObject.class, newId);
                storyObject.updatedAt = Calendar.getInstance().getTime();
                storyObject.title = title;
                storyObject.body = body;
                for (Photo photo : photos) {
                    storyObject.photos.add(new PhotoObject(photo.origin.toString(), photo.thumb.toString()));
                }
            }
        });
    }

    public void editStory(){
        if(id<=0){return;}

        RealmHelper.transaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                StoryObject storyObject = realm.where(StoryObject.class).equalTo("id", id).findFirst();
                storyObject.updatedAt = Calendar.getInstance().getTime();
                storyObject.title = title;
                storyObject.body = body;
            }
        });
    }

    public void removeStory(){
        if(id<=0){return;}

        RealmHelper.transaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                StoryObject storyObject = realm.where(StoryObject.class).equalTo("id", id).findFirst();
                storyObject.deleteFromRealm();
            }
        });
    }

    public void cancelStory(){
        if(id>0){return;}

        for(Photo photo : photos){
            photo.origin.delete();
            photo.thumb.delete();
        }

    }




    //================================================================================
    // parcelable implement
    //================================================================================
    public Story(Parcel in) {
        id = in.readLong();
        updatedAt = new Date(in.readLong());
        title = in.readString();
        body = in.readString();
        photos = new ArrayList<>();
        in.readTypedList(photos, Photo.CREATOR);
    }

    public static final Creator<Story> CREATOR = new Creator<Story>() {
        @Override
        public Story createFromParcel(Parcel in) {
            return new Story(in);
        }

        @Override
        public Story[] newArray(int size) {
            return new Story[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(updatedAt.getTime());
        dest.writeString(title);
        dest.writeString(body);
        dest.writeTypedList(photos);
    }
}
