package org.example.executiontimemeasurement.tracker;

import org.example.executiontimemeasurement.service.ExecutionTrackerService;

import java.util.function.Supplier;

public class SupplierTracker<T> extends AbstractExecutionTracker {

  public SupplierTracker(String name, ExecutionTrackerService trackerService) {
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