package com.github.bnvinay92.infinitemovies.movielist;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface MovieDbApi {

  @GET("discover/movie?api_key=df26dc63f1deaffd724609e46724ee96&language=en-US&sort_by=primary_release_date.desc")
  Flowable<MovieListResponse> fetchMovies(
      @Query("page") int page,
      @Query("primary_release_date.gte") String startDate,
      @Query("primary_release_date.lte") String endDate
  );
}
