package com.github.bnvinay92.infinitemovies.movielist;

import android.content.Context;
import com.github.bnvinay92.infinitemovies.movielist.MovieListComponent.MovieListModule;
import com.github.bnvinay92.infinitemovies.movielist.Ui.UiChange;
import com.github.bnvinay92.infinitemovies.movielist.Ui.UiEvent;
import dagger.Binds;
import dagger.BindsInstance;
import dagger.Provides;
import io.reactivex.FlowableTransformer;
import retrofit2.Retrofit;

@dagger.Subcomponent(modules = MovieListModule.class)
public interface MovieListComponent {

  void inject(MainActivity mainActivity);

  @dagger.Subcomponent.Builder
  interface Builder {

    @BindsInstance
    Builder movieListActivity(Context context);

    MovieListComponent build();
  }

  @dagger.Module
  abstract class MovieListModule {

    @Provides
    static MovieDbApi provideMovieDbApi(Retrofit retrofit) {
      return retrofit.create(MovieDbApi.class);
    }

    @Binds
    abstract FlowableTransformer<UiEvent, UiChange> provideController(MovieListController controller);

    @Binds
    abstract FlowableTransformer<MovieListRequest, MovieListResult> provideQuery(MovieListQuery usecase);
  }
}
