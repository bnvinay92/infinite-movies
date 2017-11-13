package com.github.bnvinay92.infinitemovies.movielist;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import io.reactivex.FlowableEmitter;

class InfiniteScrollListener extends OnScrollListener {

  private static final int MINIMUM_THRESHOLD = 7;

  private final LinearLayoutManager layoutManager;
  private final FlowableEmitter<Object> emitter;

  InfiniteScrollListener(LinearLayoutManager layoutManager, FlowableEmitter<Object> emitter) {
    this.layoutManager = layoutManager;
    this.emitter = emitter;
  }

  @Override
  public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    super.onScrolled(recyclerView, dx, dy);

    int numItemsBelowTheFold = calculateNumItemsBelowTheFold();

    if (numItemsBelowTheFold <= MINIMUM_THRESHOLD) {
      emitter.onNext(new Object());
    }
  }

  private int calculateNumItemsBelowTheFold() {
    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
    int pageSize = layoutManager.getChildCount();
    int totalItemCount = layoutManager.getItemCount();

    return totalItemCount - (firstVisibleItemPosition + pageSize);
  }
}
