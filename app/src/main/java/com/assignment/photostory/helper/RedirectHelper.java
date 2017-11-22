package com.assignment.photostory.helper;

import android.app.Activity;
import android.content.Intent;

import com.assignment.photostory.PhotoStoryApplication;
import com.assignment.photostory.activity.StoryActivity;
import com.assignment.photostory.activity.StoryCameraActivity;
import com.assignment.photostory.model.Story;
import com.assignment.photostory.viewmodel.activity.StoryActivityViewModel;

/**
 * Created by heeyan on 2017. 11. 23..
 */

public class RedirectHelper {
    private static final String INTENT_EXTRA_STORY = "intent.story";
    private static final String INTENT_EXTRA_STORY_ACTICITY_MODE = "intent.story.activity.mode";


    //================================================================================
    // redirecting to activities
    //================================================================================
    public static void goStoryCamera(Activity currentActivity){
        goActivity(currentActivity, StoryCameraActivity.class);
    }

    public static void goStory(Activity currentActivity, Story story, StoryActivityViewModel.MODE mode){
        Intent intent = getActivityIntent(StoryActivity.class);
        intent.putExtra(INTENT_EXTRA_STORY, story);
        intent.putExtra(INTENT_EXTRA_STORY_ACTICITY_MODE, mode);
        currentActivity.startActivity(intent);
    }

    private static void goActivity(Activity currentActivity, Class<? extends Activity> target){
        currentActivity.startActivity(getActivityIntent(target));
    }

    private static Intent getActivityIntent(Class<? extends Activity> target){
        return new Intent(PhotoStoryApplication.getContext(), target);
    }



    //================================================================================
    // parcing objects from redirecting intents
    //================================================================================
    public static Story parceStory(Intent intent){
        return (Story)intent.getParcelableExtra(INTENT_EXTRA_STORY);
    }

    public static StoryActivityViewModel.MODE parceStoryActivityMode(Intent intent){
        return (StoryActivityViewModel.MODE)intent.getSerializableExtra(INTENT_EXTRA_STORY_ACTICITY_MODE);
    }
}
