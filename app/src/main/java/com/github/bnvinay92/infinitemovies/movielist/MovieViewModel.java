package com.github.bnvinay92.infinitemovies.movielist;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class MovieViewModel {

  static MovieViewModel create(String name, String releaseDate) {
    return new AutoValue_MovieViewModel(name, releaseDate);
  }

  abstract String name();

  abstract String releaseDate();
}
