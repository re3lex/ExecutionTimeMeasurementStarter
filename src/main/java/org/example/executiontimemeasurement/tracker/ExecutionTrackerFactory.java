package org.example.executiontimemeasurement.tracker;

import lombok.RequiredArgsConstructor;
import org.example.executiontimemeasurement.service.ExecutionTrackerService;

@RequiredArgsConstructor
public class ExecutionTrackerFactory {
  private final ExecutionTrackerService trackerService;

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