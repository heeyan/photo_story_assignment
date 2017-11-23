package com.assignment.photostory.viewmodel;

import com.assignment.photostory.model.Story;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Date;

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

        //위와 같은 패턴으로 observable getter들을 테스트..
    }
}
