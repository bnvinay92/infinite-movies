package com.github.bnvinay92.infinitemovies.movielist;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.subscribers.TestSubscriber;
import org.reactivestreams.Publisher;

public class MockFlowableTransformer<T, R> implements FlowableTransformer<T, R> {

  private final PublishProcessor<R> publishProcessor = PublishProcessor.create();
  private final FlowableProcessor<T> sink = PublishProcessor.<T>create().toSerialized();
  private TestSubscriber<T> testObserver = new TestSubscriber<>();

  public MockFlowableTransformer() {
    sink.subscribe(testObserver);
  }

  public TestSubscriber<T> getTestObserver() {
    return testObserver;
  }

  public PublishProcessor<R> getPublishProcessor() {
    return publishProcessor;
  }

  @Override
  public Publisher<R> apply(Flowable<T> upstream) {
    upstream.subscribe(sink);
    return publishProcessor;
  }
}
