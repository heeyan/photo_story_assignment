package com.assignment.photostory.viewmodel.customview;

import com.assignment.photostory.model.Story;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.observers.TestObserver;

/**
 * Created by heeyan on 2017. 11. 24..
 */

public class StoryItemViewModelTest {

    @Mock
    private Story story;

    private StoryItemViewModel storyItemViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        storyItemViewModel = new StoryItemViewModel(story);
    }

    @Test
    public void testGetObservables_emitsCorrectValues() {
        //getEventObservable
        TestObserver<StoryItemViewModel.EVENT> clickEventObserver = new TestObserver<>();
        storyItemViewModel.getEventObservable().subscribe(clickEventObserver);

        clickEventObserver.assertNoErrors();
        clickEventObserver.assertEmpty();

        storyItemViewModel.itemClicked();

        clickEventObserver.assertNoErrors();
        clickEventObserver.assertValue(StoryItemViewModel.EVENT.CLICK);
    }
}
