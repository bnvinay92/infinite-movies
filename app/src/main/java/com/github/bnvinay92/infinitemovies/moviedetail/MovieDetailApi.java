package com.github.bnvinay92.infinitemovies.moviedetail;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

interface MovieDetailApi {

  @GET("movie/{id}?api_key=df26dc63f1deaffd724609e46724ee96&language=en-US")
  Single<MovieDetailResponse> fetchMovieById(@Path("id") int id);
}
