package com.assignment.photostory.viewmodel.customview;

import android.support.annotation.NonNull;

import com.assignment.photostory.model.Story;
import com.assignment.photostory.viewmodel.StoryViewModel;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by heeyan on 2017. 11. 23..
 */

public class StoryItemViewModel extends StoryViewModel {
    public enum EVENT{
        CLICK, LONG_CLICK
    }

    public String dateMonth;

    private Subject<EVENT> eventSubject = BehaviorSubject.create();

    public StoryItemViewModel(@NonNull Story story) {
        super(story);
    }

    public Observable<EVENT> getEventObservable(){
        return eventSubject;
    }

    public void itemClicked(){
        eventSubject.onNext(EVENT.CLICK);
    }

    public void itemLongClicked(){
        removeStorySync();
        eventSubject.onNext(EVENT.LONG_CLICK);
    }

    private void removeStorySync(){
        story.removeSync();
    }
}
