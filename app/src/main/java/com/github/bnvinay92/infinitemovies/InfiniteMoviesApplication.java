package com.github.bnvinay92.infinitemovies;

import android.app.Application;
import io.reactivex.plugins.RxJavaPlugins;
import timber.log.Timber;
import timber.log.Timber.DebugTree;

public class InfiniteMoviesApplication extends Application {

  private static RootComponent rootComponent;

  public static RootComponent rootComponent() {
    return rootComponent;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    Timber.plant(new DebugTree());
    RxJavaPlugins.setErrorHandler(error -> new UndeliveredException(error).flush());
    initRootComponent();
  }

  private void initRootComponent() {
    rootComponent = DaggerRootComponent.builder()
        .application(this)
        .build();
  }
}
