package com.github.bnvinay92.infinitemovies.movielist;

import com.github.bnvinay92.infinitemovies.movielist.Ui.UiChange;
import com.github.bnvinay92.infinitemovies.movielist.Ui.UiEvent;
import com.github.bnvinay92.infinitemovies.movielist.Ui.UiEvent.DateRange;
import com.github.bnvinay92.infinitemovies.movielist.Ui.UiEvent.LoadNextPage;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import javax.inject.Inject;
import org.reactivestreams.Publisher;

public class MovieListController implements FlowableTransformer<UiEvent, UiChange> {

  private final FlowableTransformer<MovieListRequest, MovieListResult> query;
  private int paginationToggle = 0;

  @Inject
  MovieListController(FlowableTransformer<MovieListRequest, MovieListResult> query) {
    this.query = query;
  }

  @Override
  public Publisher<UiChange> apply(Flowable<UiEvent> uiEvents) {
    return uiEvents.publish(uiEventMulticast -> {
      Flowable<Integer> throttledPages = streamPageRequests(uiEventMulticast);
      Flowable<DateRange> ranges = uiEventMulticast.ofType(DateRange.class);
      Flowable<DateRange> validRanges = ranges.filter(DateRange::isValid);
      return Flowable.merge(streamPaginatedMovieLists(throttledPages, validRanges), validRanges.map(o -> Ui::resetList));
    });
  }

  private Flowable<Integer> streamPageRequests(Flowable<UiEvent> uiEventMulticast) {
    return uiEventMulticast.ofType(LoadNextPage.class)
        .distinctUntilChanged(o -> paginationToggle)
        .scan(1, (page, o) -> page + 1);
  }

  private Flowable<UiChange> streamPaginatedMovieLists(Flowable<Integer> pages, Flowable<DateRange> ranges) {
    return ranges
        .distinctUntilChanged()
        .switchMap(range -> pages.map(page -> MovieListRequest.create(page, range))
            .compose(query)
            .doOnNext(result -> {
              if (!result.equals(MovieListResult.Loading.create())) {
                paginationToggle++;
              }
            }), 1);
  }
}
