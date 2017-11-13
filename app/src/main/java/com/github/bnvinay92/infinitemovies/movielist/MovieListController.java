package com.github.bnvinay92.infinitemovies.movielist;

import com.github.bnvinay92.infinitemovies.movielist.Ui.UiChange;
import com.github.bnvinay92.infinitemovies.movielist.Ui.UiEvent;
import com.github.bnvinay92.infinitemovies.movielist.Ui.UiEvent.DateRange;
import com.github.bnvinay92.infinitemovies.movielist.Ui.UiEvent.LoadNextPage;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import javax.inject.Inject;
import org.reactivestreams.Publisher;
import timber.log.Timber;

public class MovieListController implements FlowableTransformer<UiEvent, UiChange> {

  private final FlowableTransformer<MovieListRequest, MovieListResult> query;

  @Inject
  MovieListController(FlowableTransformer<MovieListRequest, MovieListResult> query) {
    this.query = query;
  }

  @Override
  public Publisher<UiChange> apply(Flowable<UiEvent> uiEvents) {
    return uiEvents.publish(uiEventMulticast -> {
      Flowable<Integer> pages = streamPageRequests(uiEventMulticast);
      Flowable<DateRange> ranges = uiEventMulticast.ofType(DateRange.class);
      return streamPaginatedMovieLists(pages, ranges);
    });
  }

  private Flowable<UiChange> streamPaginatedMovieLists(Flowable<Integer> pages, Flowable<DateRange> ranges) {
    return ranges.filter(DateRange::isValid)
        .switchMap(range -> pages.map(page -> MovieListRequest.create(page, range)))
        .doOnNext(page -> Timber.d("Page: %s", page))
        .<UiChange>compose(query)
        .doOnNext(uiChange -> Timber.d("UiChange: %s", uiChange.getClass().getSimpleName()));
  }

  private Flowable<Integer> streamPageRequests(Flowable<UiEvent> uiEventMulticast) {
    return uiEventMulticast.ofType(LoadNextPage.class)
        .scan(1, (page, ignored) -> page + 1);
  }
}
