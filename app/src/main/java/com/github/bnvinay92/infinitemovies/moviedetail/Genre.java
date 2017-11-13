package com.github.bnvinay92.infinitemovies.moviedetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Genre {

  @SerializedName("id")
  @Expose
  public int id;
  @SerializedName("name")
  @Expose
  public String name;
}
