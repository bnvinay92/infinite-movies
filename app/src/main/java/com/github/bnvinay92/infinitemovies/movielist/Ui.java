package com.github.bnvinay92.infinitemovies.movielist;

import com.google.auto.value.AutoValue;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

interface Ui {

  void addPage(List<MovieViewModel> page);

  void showTimedOut();

  void showError(String errorMessage);

  void showLoading();

  void resetList();

  interface UiChange {

    void paint(Ui ui);
  }

  interface UiEvent {

    @AutoValue
    abstract class LoadNextPage implements UiEvent {

      static LoadNextPage create() {
        return new AutoValue_Ui_UiEvent_LoadNextPage();
      }
    }

    @AutoValue
    abstract class DateRange implements UiEvent {

      private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

      static DateRange create(String startDate, String endDate) {
        return new AutoValue_Ui_UiEvent_DateRange(startDate, endDate);
      }

      abstract String start();

      abstract String end();

      // TODO: Check if start date is before end date.
      private boolean isValid(String dateString) {
        try {
          format.parse(dateString);
          return true;
        } catch (ParseException e) {
          return dateString.isEmpty();
        }
      }

      boolean isValid() {
        return isValid(start()) && isValid(end());
      }
    }
  }
}
