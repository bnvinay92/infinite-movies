package com.github.bnvinay92.infinitemovies.movielist;

import com.github.bnvinay92.infinitemovies.movielist.MovieListResult.SomeError;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import org.reactivestreams.Publisher;

public class MovieListQuery implements FlowableTransformer<MovieListRequest, MovieListResult> {

  static final int TIMEOUT = 15;

  private final MovieListApi api;

  @Inject
  MovieListQuery(MovieListApi api) {
    this.api = api;
  }

  @Override
  public Publisher<MovieListResult> apply(Flowable<MovieListRequest> actions) {
    return actions.flatMap(action ->
        api.fetchMovies(action.page(), action.startDate(), action.endDate())
            .subscribeOn(Schedulers.io())
            .map(this::createResult)
            .timeout(15, TimeUnit.SECONDS, Flowable.just(MovieListResult.TimedOut.create()))
            .onErrorReturn(SomeError::create)
            .observeOn(AndroidSchedulers.mainThread())
            .startWith(MovieListResult.Loading.create()), 1);
  }

  private MovieListResult createResult(MovieListResponse response) {
    List<MovieViewModel> viewModels = new ArrayList<>();
    for (Movie movie : response.movies) {
      viewModels.add(MovieViewModel.create(movie));
    }
    return MovieListResult.Success.create(viewModels);
  }
}
