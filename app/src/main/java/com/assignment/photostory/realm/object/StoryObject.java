package com.assignment.photostory.realm.object;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by heeyan on 2017. 11. 20..
 */

public class StoryObject extends RealmObject {
    @PrimaryKey
    public long id;
    public String title;
    public String body;
    public RealmList<PhotoObject> photos;
}
