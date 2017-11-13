package com.github.bnvinay92.infinitemovies.movielist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.github.bnvinay92.infinitemovies.InfiniteMoviesApplication;
import com.github.bnvinay92.infinitemovies.R;
import com.github.bnvinay92.infinitemovies.movielist.Ui.UiEvent.DateRange;
import com.github.bnvinay92.infinitemovies.movielist.Ui.UiEvent.LoadNextPage;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements Ui {

  @BindView(R.id.rview_movies) RecyclerView recyclerView;
  @BindView(R.id.etext_startdate) EditText startDateView;
  @BindView(R.id.etext_enddate) EditText endDateView;
  @BindView(R.id.button_submit) Button submitView;

  @Inject FlowableTransformer<UiEvent, UiChange> controller;

  private LinearLayoutManager layoutManager;
  private Disposable disposable;

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

    disposable = streamUiEvents()
        .compose(controller)
        .subscribe(uiChange -> uiChange.paint(this));
  }

  private void initRecyclerView() {
    layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    disposable.dispose();
  }

  private Flowable<UiEvent> streamUiEvents() {
    return Flowable.merge(nextPageRequests(), dateRangeSubmissions());
  }

  private Flowable<UiEvent.LoadNextPage> nextPageRequests() {
    return Flowable.create(emitter -> {
      InfiniteScrollListener<LoadNextPage> listener = new InfiniteScrollListener<>(layoutManager, emitter);
      emitter.setCancellable(() -> recyclerView.removeOnScrollListener(listener));
      recyclerView.addOnScrollListener(listener);
    }, BackpressureStrategy.DROP);
  }

  private Flowable<DateRange> dateRangeSubmissions() {
    return RxView.clicks(submitView)
        .map(__ -> DateRange.create(getString(startDateView), getString(endDateView)))
        .toFlowable(BackpressureStrategy.LATEST);
  }

  @NonNull
  private String getString(EditText dateView) {
    return dateView.getText().toString().trim();
  }
}
