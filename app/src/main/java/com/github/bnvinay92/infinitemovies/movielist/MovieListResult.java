package com.github.bnvinay92.infinitemovies.movielist;

import com.github.bnvinay92.infinitemovies.movielist.Ui.UiChange;
import com.google.auto.value.AutoValue;
import java.util.List;

interface MovieListResult extends UiChange {

  @AutoValue
  abstract class Loading implements MovieListResult {

    static Loading create() {
      return new AutoValue_MovieListResult_Loading();
    }

    @Override
    public void paint(Ui ui) {
      ui.showLoading();
    }
  }

  @AutoValue
  abstract class Success implements MovieListResult {

    static Success create(List<MovieViewModel> page) {
      return new AutoValue_MovieListResult_Success(page);
    }

    abstract List<MovieViewModel> page();

    @Override
    public void paint(Ui ui) {
      ui.addPage(page());
    }
  }

  @AutoValue
  abstract class TimedOut implements MovieListResult {

    static TimedOut create() {
      return new AutoValue_MovieListResult_TimedOut();
    }

    @Override
    public void paint(Ui ui) {
      ui.showTimedOut();
    }
  }

  @AutoValue
  abstract class SomeError implements MovieListResult {

    static SomeError create(Throwable throwable) {
      return new AutoValue_MovieListResult_SomeError(throwable);
    }

    abstract Throwable throwable();

    @Override
    public void paint(Ui ui) {
      ui.showError(throwable().getMessage());
    }
  }
}
