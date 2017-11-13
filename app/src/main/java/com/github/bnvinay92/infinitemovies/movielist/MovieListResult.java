package com.github.bnvinay92.infinitemovies.movielist;

import com.github.bnvinay92.infinitemovies.movielist.Ui.UiChange;
import com.google.auto.value.AutoValue;

interface MovieListResult extends UiChange {

  @AutoValue
  abstract class Loading implements MovieListResult {

    static Loading create() {
      return new AutoValue_MovieListResult_Loading();
    }

    @Override
    public void paint(Ui ui) {

    }
  }

  @AutoValue
  abstract class Success implements MovieListResult {

    static Success create() {
      return new AutoValue_MovieListResult_Success();
    }

    @Override
    public void paint(Ui ui) {

    }
  }

  @AutoValue
  abstract class TimedOut implements MovieListResult {

    static TimedOut create() {
      return new AutoValue_MovieListResult_TimedOut();
    }

    @Override
    public void paint(Ui ui) {

    }
  }

  @AutoValue
  abstract class Error implements MovieListResult {

    static Error create(Throwable throwable) {
      return new AutoValue_MovieListResult_Error(throwable);
    }

    abstract Throwable throwable();

    @Override
    public void paint(Ui ui) {

    }
  }
}
