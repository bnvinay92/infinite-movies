package com.github.bnvinay92.infinitemovies.moviedetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.github.bnvinay92.infinitemovies.InfiniteMoviesApplication;
import com.github.bnvinay92.infinitemovies.R;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import timber.log.Timber;

public class MovieDetailActivity extends AppCompatActivity {

  static final String KEY_NAME = "name";
  static final String KEY_ID = "id";

  @BindView(R.id.tview_moviedetail) TextView textView;

  @Inject MovieDetailApi api;
  private Disposable disposable;

  public static void start(Context context, int id, String name) {
    Intent intent = new Intent(context, MovieDetailActivity.class);
    intent.putExtra(KEY_NAME, name);
    intent.putExtra(KEY_ID, id);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    InfiniteMoviesApplication.rootComponent()
        .movieDetailComponentBuilder()
        .movieDetailActivity(this)
        .build()
        .inject(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_detail);
    ButterKnife.bind(this);

    disposable = api.fetchMovieById(getIntent().getIntExtra(KEY_ID, -1))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::showMovie, Timber::e);
  }

  private void showMovie(MovieDetailResponse movie) {
    textView.setText(movie.toString());
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    disposable.dispose();
  }
}
