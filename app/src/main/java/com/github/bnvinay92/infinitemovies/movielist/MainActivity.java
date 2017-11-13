package com.github.bnvinay92.infinitemovies.movielist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnChildAttachStateChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.github.bnvinay92.infinitemovies.InfiniteMoviesApplication;
import com.github.bnvinay92.infinitemovies.R;
import com.github.bnvinay92.infinitemovies.moviedetail.MovieDetailActivity;
import com.github.bnvinay92.infinitemovies.movielist.Ui.UiEvent.DateRange;
import com.github.bnvinay92.infinitemovies.movielist.Ui.UiEvent.LoadNextPage;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements Ui {

  @BindView(R.id.rview_movies) RecyclerView recyclerView;
  @BindView(R.id.etext_startdate) EditText startDateView;
  @BindView(R.id.etext_enddate) EditText endDateView;
  @BindView(R.id.button_submit) Button submitView;

  @Inject FlowableTransformer<UiEvent, UiChange> controller;

  private List<MovieViewModel> movies = new ArrayList<>();
  private MoviesAdapter adapter = new MoviesAdapter(movies);
  private LinearLayoutManager layoutManager;
  private Disposable onDestroyDisposable;
  private Disposable onStopDisposable;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    InfiniteMoviesApplication.rootComponent()
        .movieListComponentBuilder()
        .movieListActivity(this)
        .build()
        .inject(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    initRecyclerView();

    onDestroyDisposable = streamUiEvents()
        .doOnNext(uiEvent -> Timber.d(uiEvent.getClass().getSimpleName()))
        .compose(controller)
        .subscribe(
            uiChange -> uiChange.paint(this),
            Timber::e);
  }

  private void initRecyclerView() {
    layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);
  }

  @Override
  protected void onStart() {
    super.onStart();
    onStopDisposable = streamItemClicks()
        .map(adapterPosition -> movies.get(adapterPosition))
        .subscribe(movie -> MovieDetailActivity.start(this, movie.id(), movie.name()));
  }

  @Override
  protected void onStop() {
    super.onStop();
    onStopDisposable.dispose();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    onDestroyDisposable.dispose();
  }

  private Flowable<UiEvent> streamUiEvents() {
    return Flowable.merge(nextPageRequests(), dateRangeSubmissions());
  }

  private Flowable<UiEvent.LoadNextPage> nextPageRequests() {
    return Flowable.create(emitter -> {
      InfiniteScrollListener listener = new InfiniteScrollListener(layoutManager, emitter);
      emitter.setCancellable(() -> recyclerView.removeOnScrollListener(listener));
      recyclerView.addOnScrollListener(listener);
    }, BackpressureStrategy.DROP)
        .map(__ -> LoadNextPage.create());
  }

  private Flowable<DateRange> dateRangeSubmissions() {
    return RxView.clicks(submitView)
        .map(__ -> DateRange.create(getString(startDateView), getString(endDateView)))
        .startWith(DateRange.create("", ""))
        .toFlowable(BackpressureStrategy.LATEST);
  }

  @NonNull
  private String getString(EditText dateView) {
    return dateView.getText().toString().trim();
  }

  private Observable<Integer> streamItemClicks() {
    return Observable.<View>create(emitter -> {
      OnChildAttachStateChangeListener listener = new OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(View view) {
          view.setOnClickListener(emitter::onNext);
        }

        @Override
        public void onChildViewDetachedFromWindow(View view) {

        }
      };
      emitter.setCancellable(() -> recyclerView.removeOnChildAttachStateChangeListener(listener));
      recyclerView.addOnChildAttachStateChangeListener(listener);
    }).map(view -> recyclerView.getChildAdapterPosition(view));
  }

  @Override
  public void addPage(List<MovieViewModel> moviePage) {
    int currentSize = movies.size();
    movies.addAll(moviePage);
    adapter.notifyItemRangeInserted(currentSize, moviePage.size());
  }

  @Override
  public void showTimedOut() {
    Toast.makeText(this, "Next page timed out!", Toast.LENGTH_LONG).show();
  }

  @Override
  public void showError(String errorMessage) {
  }

  @Override
  public void showLoading() {
    Toast.makeText(this, "Loading next page...", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void resetList() {
    int currentSize = movies.size();
    movies.clear();
    adapter.notifyItemRangeRemoved(0, currentSize);
  }
}
