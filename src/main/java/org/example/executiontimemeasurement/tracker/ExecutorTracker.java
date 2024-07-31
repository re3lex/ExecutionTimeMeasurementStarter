package org.example.executiontimemeasurement.tracker;

import org.example.executiontimemeasurement.service.ExecutionTrackerService;
import org.springframework.util.StopWatch;

public class ExecutorTracker extends AbstractExecutionTracker {

  public ExecutorTracker(String name, ExecutionTrackerService trackerService) {
    super(name, trackerService);
  }

  public void measure(Runnable runnable) {
    StopWatch stopWatch = start();
    try {
      runnable.run();
      finish(stopWatch, false);
    } catch (Exception e) {
      finish(stopWatch, true);
      throw e;
    }
  }
}