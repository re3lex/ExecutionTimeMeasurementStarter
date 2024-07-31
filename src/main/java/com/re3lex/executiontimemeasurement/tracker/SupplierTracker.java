package com.re3lex.executiontimemeasurement.tracker;

import com.re3lex.executiontimemeasurement.service.ExecTimeTrackerService;

import java.util.function.Supplier;

public class SupplierTracker<T> extends AbstractExecutionTracker {

  public SupplierTracker(String name, ExecTimeTrackerService trackerService) {
    super(name, trackerService);
  }

  public T measure(Supplier<T> supplier) {
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
}