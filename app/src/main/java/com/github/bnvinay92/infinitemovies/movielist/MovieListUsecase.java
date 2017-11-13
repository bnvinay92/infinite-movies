package com.github.bnvinay92.infinitemovies.movielist;

import com.github.bnvinay92.infinitemovies.movielist.MovieListResult.Error;
import com.github.bnvinay92.infinitemovies.movielist.MovieListResult.Success;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Publisher;

public class MovieListUsecase implements FlowableTransformer<MovieListRequest, MovieListResult> {

  static final int TIMEOUT = 15;

  private final MovieDbApi api;

  public MovieListUsecase(MovieDbApi api) {
    this.api = api;
  }

  @Override
  public Publisher<MovieListResult> apply(Flowable<MovieListRequest> actions) {
    return actions.flatMap(action ->
        api.fetchMovies(action.page(), action.startDate(), action.endDate())
            .subscribeOn(Schedulers.io())
            .<MovieListResult>map(response -> Success.create())
            .timeout(15, TimeUnit.SECONDS, Flowable.just(MovieListResult.TimedOut.create()))
            .onErrorReturn(Error::create)
            .observeOn(AndroidSchedulers.mainThread())
            .startWith(MovieListResult.Loading.create()), 1);
  }
}
