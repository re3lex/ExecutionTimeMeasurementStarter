package org.example.executiontimemeasurement.tracker;

import org.example.executiontimemeasurement.service.ExecutionTrackerService;
import org.springframework.util.StopWatch;

import java.util.function.Supplier;

public class SupplierTracker<T> extends AbstractExecutionTracker {

  public SupplierTracker(String name, ExecutionTrackerService trackerService) {
    super(name, trackerService);
  }

  public T measure(Supplier<T> supplier) {
    StopWatch stopWatch = start();
    T result;

    try {
      result = supplier.get();
    } catch (Exception e) {
      finish(stopWatch, true);
      throw e;
    }

    finish(stopWatch, false);
    return result;
  }
}