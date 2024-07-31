package com.re3lex.executiontimemeasurement.tracker;

import lombok.RequiredArgsConstructor;
import com.re3lex.executiontimemeasurement.service.ExecTimeTrackerService;

@RequiredArgsConstructor
public class ExecTimeTrackerFactory {
  private final ExecTimeTrackerService trackerService;

  public <T> SupplierTracker<T> supplierTracker(String name) {
    return new SupplierTracker<>(name, trackerService);
  }

  public <T> ThrowingSupplierTracker<T> throwingSupplierTracker(String name) {
    return new ThrowingSupplierTracker<>(name, trackerService);
  }

  public ExecutorTracker executorTracker(String name) {
    return new ExecutorTracker(name, trackerService);
  }
}