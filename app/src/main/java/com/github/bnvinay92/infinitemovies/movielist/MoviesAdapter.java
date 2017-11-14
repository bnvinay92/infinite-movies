package com.github.bnvinay92.infinitemovies.movielist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.github.bnvinay92.infinitemovies.R;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import java.util.List;

class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

  private final List<MovieViewModel> movies;
  private PublishSubject<Integer> itemClicks = PublishSubject.create();

  MoviesAdapter(List<MovieViewModel> movies) {
    this.movies = movies;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View itemView = inflater.inflate(R.layout.item_movie, parent, false);
    return new ViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.bind(movies.get(position));
  }

  @Override
  public int getItemCount() {
    return movies.size();
  }

  public Observable<Integer> itemClicks() {
    return itemClicks;
  }

  class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

    @BindView(R.id.tview_movie_name) TextView nameView;
    @BindView(R.id.tview_movie_releasedate) TextView releaseDateView;

    ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    void bind(MovieViewModel movie) {
      nameView.setText(movie.name());
      releaseDateView.setText(movie.releaseDate());
    }

    @Override
    public void onClick(View v) {
      itemClicks.onNext(getAdapterPosition());
    }
  }
}
