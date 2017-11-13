package com.github.bnvinay92.infinitemovies.movielist;

import com.github.bnvinay92.infinitemovies.movielist.Ui.UiChange;
import com.github.bnvinay92.infinitemovies.movielist.Ui.UiEvent;
import com.github.bnvinay92.infinitemovies.movielist.Ui.UiEvent.DateRange;
import com.github.bnvinay92.infinitemovies.movielist.Ui.UiEvent.LoadNextPage;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.subscribers.TestSubscriber;
import java.util.Collections;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class MovieListControllerShould {

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

  MovieListController controller;
  MockFlowableTransformer<MovieListRequest, MovieListResult> query = new MockFlowableTransformer<>();

  PublishProcessor<UiEvent> uiEvents = PublishProcessor.create();

  DateRange validRange = DateRange.create("2016-06-16", "2017-06-16");
  DateRange invalidRange = DateRange.create("a", "05-06-2016");
  DateRange validRange2 = DateRange.create("", "1993-10-21");
  private TestSubscriber<UiChange> observer;

  @Before
  public void setUp() throws Exception {
    controller = new MovieListController(query);
    observer = uiEvents.compose(controller).test();
  }

  private void setDateRange(DateRange validRange) {
    uiEvents.onNext(validRange);
  }

  private void scroll() {
    uiEvents.onNext(LoadNextPage.create());
  }

  @Test
  public void when_page_scrolled_then_fetch_next_page() throws Exception {
    setDateRange(validRange);
    scroll();

    query.getTestObserver().assertValues(
        MovieListRequest.create(1, validRange),
        MovieListRequest.create(2, validRange));
  }

  @Test
  public void when_dates_changed_then_reset_page_counter() throws Exception {
    setDateRange(validRange);
    scroll();
    setDateRange(validRange2);

    query.getTestObserver().assertValues(
        MovieListRequest.create(1, validRange),
        MovieListRequest.create(2, validRange),
        MovieListRequest.create(1, validRange2));
  }

  @Test
  public void given_api_in_flight_ignore_scroll_events_till_page_load() throws Exception {
    setDateRange(validRange);
    scroll();
    scroll();
    scroll();
    scroll();
    query.getPublishProcessor().onNext(MovieListResult.Success.create(Collections.emptyList()));

    query.getTestObserver().assertValues(
        MovieListRequest.create(1, validRange),
        MovieListRequest.create(2, validRange));
  }

  @Test
  public void when_invalid_date_chosen_then_dont_reset_page_counter() throws Exception {
    setDateRange(validRange2);
    setDateRange(invalidRange);
    scroll();

    query.getTestObserver().assertValues(
        MovieListRequest.create(1, validRange2),
        MovieListRequest.create(2, validRange2));
  }

  @After
  public void tearDown() {
    Assert.assertFalse(observer.isDisposed());
    observer.assertNoErrors();
  }
}