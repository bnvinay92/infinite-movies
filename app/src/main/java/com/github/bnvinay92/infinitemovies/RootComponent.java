package com.github.bnvinay92.infinitemovies;

import android.app.Application;
import com.github.bnvinay92.infinitemovies.RootComponent.RootModule;
import com.github.bnvinay92.infinitemovies.moviedetail.MovieDetailComponent;
import com.github.bnvinay92.infinitemovies.movielist.MovieListComponent;
import com.google.gson.Gson;
import dagger.BindsInstance;
import dagger.Provides;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

@Singleton
@dagger.Component(modules = RootModule.class)
public interface RootComponent {

  MovieListComponent.Builder movieListComponentBuilder();

  MovieDetailComponent.Builder movieDetailComponentBuilder();

  @dagger.Component.Builder
  interface Builder {

    @BindsInstance
    Builder application(InfiniteMoviesApplication application);

    RootComponent build();
  }

  @dagger.Module
  abstract class RootModule {

    @Provides
    static Application provideApplication(InfiniteMoviesApplication application) {
      return application;
    }

    @Provides
    @Singleton
    static OkHttpClient provideOkHttpClient() {
      HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> Timber.tag("OkHttp").d(message));
      logging.setLevel(Level.BASIC);

      return new OkHttpClient.Builder()
          .addInterceptor(logging)
          .build();
    }

    @Provides
    @Singleton
    static Retrofit provideRetrofit(OkHttpClient okHttpClient, Gson gson) {
      return new Retrofit.Builder()
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .addConverterFactory(GsonConverterFactory.create(gson))
          .baseUrl("https://api.themoviedb.org/3/")
          .client(okHttpClient)
          .validateEagerly(true)
          .build();
    }

    @Provides
    @Singleton
    static Gson provideGson() {
      return new Gson();
    }
  }
}
