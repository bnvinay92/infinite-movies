package com.github.bnvinay92.infinitemovies.movielist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MovieListResponse {

  @SerializedName("page")
  @Expose
  public int page;
  @SerializedName("total_results")
  @Expose
  public int totalResults;
  @SerializedName("total_pages")
  @Expose
  public int totalPages;
  @SerializedName("results")
  @Expose
  public List<Movie> movies;
}
