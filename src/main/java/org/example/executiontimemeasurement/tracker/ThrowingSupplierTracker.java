package org.example.executiontimemeasurement.tracker;

import org.example.executiontimemeasurement.service.ExecutionTrackerService;
import org.springframework.util.StopWatch;

public class ThrowingSupplierTracker<T> extends AbstractExecutionTracker {

  public ThrowingSupplierTracker(String name, ExecutionTrackerService trackerService) {
    super(name, trackerService);
  }

  public T measure(ThrowingSupplier<T> supplier) throws Throwable {
    StopWatch stopWatch = start();
    T result;

    try {
      result = supplier.get();
    } catch (Throwable e) {
      finish(stopWatch, true);
      throw e;
    }

    finish(stopWatch, false);
    return result;
  }

  public interface ThrowingSupplier<T> {
    T get() throws Throwable;
  }
}