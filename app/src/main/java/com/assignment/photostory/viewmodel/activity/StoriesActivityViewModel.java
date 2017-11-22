package com.assignment.photostory.viewmodel.activity;

import android.support.annotation.NonNull;

import com.assignment.photostory.model.Stories;
import com.assignment.photostory.model.Story;
import com.assignment.photostory.viewmodel.ViewModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by heeyan on 2017. 11. 21..
 */

public class StoriesActivityViewModel extends ViewModel {
    @NonNull
    private final Stories stories;

    private Subject<List<Story>> storiesSubject = BehaviorSubject.create();

    public StoriesActivityViewModel(@NonNull Stories stories) {
        this.stories = stories;
        storiesSubject.onNext(this.stories.stories);
    }

    public Observable<List<Story>> getStoriesObservable(){
        return storiesSubject;
    }

    public void search(String query){
        stories.search(query);
        storiesSubject.onNext(stories.stories);
    }

    public void refreshStories(){
        stories.search(null);
        storiesSubject.onNext(stories.stories);
    }
}
