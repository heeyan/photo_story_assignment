package com.assignment.photostory.viewmodel.activity;

import com.assignment.photostory.model.Photo;
import com.assignment.photostory.model.Story;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import io.reactivex.observers.TestObserver;

/**
 * Created by heeyan on 2017. 11. 23..
 */

public class StoryActivityViewModelTest {

    @Mock
    private Story story;

    private StoryActivityViewModel storyActivityViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        storyActivityViewModel = new StoryActivityViewModel(story, StoryActivityViewModel.MODE.WRITE);
    }

    @Test
    public void testGetObservables_emitsCorrectValues() {
        //getModeObservable
        TestObserver<StoryActivityViewModel.MODE> modeObserver = new TestObserver<>();
        storyActivityViewModel.getModeObservable().subscribe(modeObserver);

        modeObserver.assertNoErrors();
        modeObserver.assertValue(StoryActivityViewModel.MODE.WRITE);

        //getPhotoCountObservable
        List<Photo> photos = Arrays.asList(
                new Photo(new File("origin1.jpeg"), new File("thumb1.jpeg")),
                new Photo(new File("origin2.jpeg"), new File("thumb2.jpeg")));
        Mockito.when(story.getPhotos()).thenReturn(photos);

        TestObserver<Integer> photoCountObserver = new TestObserver<>();
        storyActivityViewModel.getPhotoCountObservable().subscribe(photoCountObserver);

        photoCountObserver.assertNoErrors();
        photoCountObserver.assertEmpty();

        storyActivityViewModel.addPhoto(null);

        photoCountObserver.assertNoErrors();
        photoCountObserver.assertValue(2);
    }




}
