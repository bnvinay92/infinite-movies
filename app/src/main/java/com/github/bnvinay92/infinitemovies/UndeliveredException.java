package com.github.bnvinay92.infinitemovies;

import java.io.IOException;
import timber.log.Timber;

class UndeliveredException {

  private final Throwable undeliveredError;

  UndeliveredException(Throwable undeliveredError) {
    this.undeliveredError = undeliveredError;
  }

  void flush() {
    if (isIgnorable()) {
      return;
    }
    if (isBug()) {
      Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), undeliveredError);
      return;
    }
    Timber.w(undeliveredError, "Undeliverable exception received!");
  }

  private boolean isIgnorable() {
    return undeliveredError instanceof IOException
        || undeliveredError instanceof InterruptedException;
  }

  private boolean isBug() {
    return undeliveredError instanceof NullPointerException
        || undeliveredError instanceof IllegalArgumentException
        || undeliveredError instanceof IllegalStateException;
  }
}
