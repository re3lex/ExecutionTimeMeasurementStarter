package com.re3lex.executiontimemeasurement.tracker;

import com.re3lex.executiontimemeasurement.service.ExecTimeTrackerService;

public class ThrowingSupplierTracker<T> extends AbstractExecutionTracker {

  public ThrowingSupplierTracker(String name, ExecTimeTrackerService trackerService) {
    super(name, trackerService);
  }

  public T measure(ThrowingSupplier<T> supplier) throws Throwable {
    start();
    boolean withError = false;

    try {
      return supplier.get();
    } catch (Exception e) {
      withError = true;
      throw e;
    } finally {
      finish(withError);
    }
  }

  @FunctionalInterface
  public interface ThrowingSupplier<T> {
    T get() throws Throwable;
  }
}