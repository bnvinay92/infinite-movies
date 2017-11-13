package com.github.bnvinay92.infinitemovies.movielist;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.github.bnvinay92.infinitemovies.TestSchedulerRule;
import com.github.bnvinay92.infinitemovies.movielist.MovieListResult.Loading;
import com.github.bnvinay92.infinitemovies.movielist.MovieListResult.Success;
import com.github.bnvinay92.infinitemovies.movielist.MovieListResult.TimedOut;
import com.github.bnvinay92.infinitemovies.movielist.Ui.UiEvent.DateRange;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.subscribers.TestSubscriber;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class MovieListQueryShould {

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Rule public TestSchedulerRule testSchedulerRule = new TestSchedulerRule();

  @InjectMocks MovieListQuery usecase;
  @Mock MovieDbApi api;

  PublishProcessor<MovieListRequest> requests = PublishProcessor.create();
  PublishProcessor<String> responses = PublishProcessor.create();

  TestSubscriber<MovieListResult> testSubscriber;
  MovieListRequest request = MovieListRequest.create(1, DateRange.create("", ""));

  @Before
  public void setUp() throws Exception {
    testSubscriber = requests.onBackpressureDrop().compose(usecase).test();
  }

  @Test
  public void given_api_in_flight_when_request_received_then_drop_request() throws Exception {
    when(api.fetchMovies(anyInt(), anyString(), anyString())).thenReturn(responses);

    requests.onNext(request);
    requests.onNext(request);
    responses.onNext("");

    testSubscriber.assertValues(Loading.create(), Success.create());
  }

  @Test
  public void given_api_takes_too_long_then_return_timed_out() {
    when(api.fetchMovies(anyInt(), anyString(), anyString())).thenReturn(responses);

    requests.onNext(request);
    testSchedulerRule.getTestScheduler().advanceTimeBy(MovieListQuery.TIMEOUT, TimeUnit.SECONDS);

    testSubscriber.assertValues(Loading.create(), TimedOut.create());
  }
}