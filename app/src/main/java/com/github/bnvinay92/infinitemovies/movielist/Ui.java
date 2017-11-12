package com.github.bnvinay92.infinitemovies.movielist;

import com.google.auto.value.AutoValue;

interface Ui {

  interface UiChange {

    void paint(Ui ui);
  }

  interface UiEvent {

    interface LoadNextPage extends UiEvent {}

    @AutoValue
    abstract class DateRange implements UiEvent {

      static DateRange create(String startDate, String endDate) {
        return new AutoValue_Ui_UiEvent_DateRange(startDate, endDate);
      }

      abstract String startDate();

      abstract String endDate();

      boolean isValid() {
        return true;
      }
    }
  }
}
