package com.github.bnvinay92.infinitemovies.movielist;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class MovieViewModel {

  abstract String name();

  abstract String releaseDate();

  static MovieViewModel create(Movie movie) {
    return null;
  }
}
