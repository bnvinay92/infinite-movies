package com.github.bnvinay92.infinitemovies.movielist;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import io.reactivex.FlowableEmitter;

class InfiniteScrollListener<T> extends OnScrollListener {

  private static final int MINIMUM_THRESHOLD = 7;

  private final LinearLayoutManager layoutManager;
  private final FlowableEmitter<? super T> emitter;

  InfiniteScrollListener(LinearLayoutManager layoutManager, FlowableEmitter<? super T> emitter) {
    this.layoutManager = layoutManager;
    this.emitter = emitter;
  }

  @Override
  public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    super.onScrolled(recyclerView, dx, dy);

    int numItemsBelowTheFold = calculateNumItemsBelowTheFold();

    if (numItemsBelowTheFold <= MINIMUM_THRESHOLD) {
      emitter.onNext((T) new Object());
    }
  }

  private int calculateNumItemsBelowTheFold() {
    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
    int pageSize = layoutManager.getChildCount();
    int totalItemCount = layoutManager.getItemCount();

    return totalItemCount - (firstVisibleItemPosition + pageSize);
  }
}
