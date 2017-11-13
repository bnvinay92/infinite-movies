package com.github.bnvinay92.infinitemovies.moviedetail;

import android.content.Context;
import com.github.bnvinay92.infinitemovies.moviedetail.MovieDetailComponent.MovieDetailModule;
import dagger.BindsInstance;
import dagger.Provides;
import retrofit2.Retrofit;

@dagger.Subcomponent(modules = MovieDetailModule.class)
public interface MovieDetailComponent {

  void inject(MovieDetailActivity target);

  @dagger.Subcomponent.Builder
  interface Builder {

    @BindsInstance
    MovieDetailComponent.Builder movieDetailActivity(Context context);

    MovieDetailComponent build();
  }


  @dagger.Module
  abstract class MovieDetailModule {

    @Provides
    static MovieDetailApi provideMovieDbApi(Retrofit retrofit) {
      return retrofit.create(MovieDetailApi.class);
    }

  }
}
