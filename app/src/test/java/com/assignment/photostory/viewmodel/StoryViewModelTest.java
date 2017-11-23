package com.assignment.photostory.viewmodel;

import com.assignment.photostory.model.Photo;
import com.assignment.photostory.model.Story;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.observers.TestObserver;

/**
 * Created by heeyan on 2017. 11. 23..
 */

public class StoryViewModelTest {

    @Mock
    private Story story;

    private StoryViewModel storyViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        storyViewModel = new StoryViewModel(story);
    }

    @Test
    public void testGetObservables_emitsCorrectValues() {
        //getUpdatedAtObservable
        Date testDate = Calendar.getInstance().getTime();
        Mockito.when(story.getUpdatedAt()).thenReturn(testDate);

        TestObserver<Date> updatedAtObserver = new TestObserver<>();
        storyViewModel.getUpdatedAtObservable().subscribe(updatedAtObserver);

        updatedAtObserver.assertNoErrors();
        updatedAtObserver.assertValue(testDate);

        //getTitleObservable
        String title = "title";
        Mockito.when(story.getTitle()).thenReturn(title);

        TestObserver<String> titleObserver = new TestObserver<>();
        storyViewModel.getTitleObservable().subscribe(titleObserver);

        titleObserver.assertNoErrors();
        titleObserver.assertValue(title);

        //getBodyObservable
        String body = "body";
        Mockito.when(story.getBody()).thenReturn(body);

        TestObserver<String> bodyObserver = new TestObserver<>();
        storyViewModel.getBodyObservable().subscribe(bodyObserver);

        bodyObserver.assertNoErrors();
        bodyObserver.assertValue(body);

        //getPhotosObservable
        List<Photo> photos = Arrays.asList(
                new Photo(new File("origin1.jpeg"), new File("thumb1.jpeg")),
                new Photo(new File("origin2.jpeg"), new File("thumb2.jpeg")));
        Mockito.when(story.getPhotos()).thenReturn(photos);

        TestObserver<List<Photo>> photosObserver = new TestObserver<>();
        storyViewModel.getPhotosObservable().subscribe(photosObserver);

        photosObserver.assertNoErrors();
        photosObserver.assertValue(photos);
    }
}
