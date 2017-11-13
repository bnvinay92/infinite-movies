package com.github.bnvinay92.infinitemovies.movielist;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class MovieViewModel {

  static MovieViewModel create(Movie movie) {
    return new AutoValue_MovieViewModel(movie.id, movie.title, movie.releaseDate);
  }

  abstract int id();

  abstract String name();

  abstract String releaseDate();
}
