package com.github.bnvinay92.infinitemovies.movielist;

import com.github.bnvinay92.infinitemovies.movielist.Ui.UiEvent.DateRange;
import com.google.auto.value.AutoValue;

@AutoValue
abstract class MovieListRequest {

  static MovieListRequest create(int page, DateRange dateRange) {
    return new AutoValue_MovieListRequest(page, dateRange.start(), dateRange.end());
  }

  abstract int page();

  abstract String startDate();

  abstract String endDate();
}
