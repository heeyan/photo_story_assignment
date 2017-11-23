package com.assignment.photostory.model;

import android.os.Parcel;
import android.os.Parcelable;

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
    private Date updatedAt;
    private String title;
    private String body;
    private List<Photo> photos;


    //================================================================================
    // constructors......
    //================================================================================
    //for new story
    public Story() {
        id = -1;
        updatedAt = Calendar.getInstance().getTime();
        title = "";
        body = "";
        photos = new ArrayList<>();
    }

    //for exist story
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



    //================================================================================
    // getters, setters
    //================================================================================
    public long getId() {
        return id;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }



    //================================================================================
    // action logic for story
    //================================================================================
    public void addPhoto(File photo){
        photos.add(new Photo(photo));
    }

    public void save(){
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
                    storyObject.photos.add(new PhotoObject(photo));
                }
            }
        });
    }

    public void edit(){
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

    public void remove(){
        if(id<=0){return;}

        RealmHelper.transaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                StoryObject storyObject = realm.where(StoryObject.class).equalTo("id", id).findFirst();
                storyObject.deleteFromRealm();
            }
        });
    }

    public void removeSync(){
        if(id<=0){return;}

        Realm realm = RealmHelper.getRealm();
        realm.beginTransaction();
        StoryObject storyObject = realm.where(StoryObject.class).equalTo("id", id).findFirst();
        storyObject.deleteFromRealm();
        realm.commitTransaction();
    }

    public void cancel(){
        if(id>0){return;}

        for(Photo photo : photos){
            photo.removePhoto();
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
