package com.assignment.photostory.model;

import com.assignment.photostory.helper.RealmHelper;
import com.assignment.photostory.realm.object.StoryObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by heeyan on 2017. 11. 21..
 */

public class Stories {
    public List<Story> stories = new ArrayList<>();

    private String query;

    public Stories() {
        setStoriesForSearchQuery(null);
    }

    public Stories(String query) {
        setStoriesForSearchQuery(query);
    }

    public void search(String query){
        stories.clear();
        setStoriesForSearchQuery(query);
    }

    private void setStoriesForSearchQuery(String query){
        RealmResults<StoryObject> storyObjects;
        if(query == null || query.isEmpty()){
            storyObjects = RealmHelper.getRealm().where(StoryObject.class)
                    .findAll()
                    .sort("updatedAt", Sort.DESCENDING);
        }else{
            storyObjects = RealmHelper.getRealm().where(StoryObject.class)
                    .contains("title", query)
                    .or()
                    .contains("body", query)
                    .findAll()
                    .sort("updatedAt", Sort.DESCENDING);
        }

        for(StoryObject storyObject : storyObjects){
            Story story = new Story(storyObject.id);
            stories.add(story);
        }
    }
}
