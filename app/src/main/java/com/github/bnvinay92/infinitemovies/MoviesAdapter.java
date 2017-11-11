package com.github.bnvinay92.infinitemovies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return null;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return 0;
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tview_movie_name) TextView nameView;
    @BindView(R.id.tview_movie_releasedate) TextView releaseDateView;

    ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(itemView);
    }
  }
}
