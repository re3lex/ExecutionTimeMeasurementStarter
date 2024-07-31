package com.re3lex.executiontimemeasurement.tracker;

import com.re3lex.executiontimemeasurement.service.ExecTimeTrackerService;

public class ExecutorTracker extends AbstractExecutionTracker {

  public ExecutorTracker(String name, ExecTimeTrackerService trackerService) {
    super(name, trackerService);
  }

  public void measure(Runnable runnable) {
    start();
    boolean withError = false;
    try {
      runnable.run();
    } catch (Exception e) {
      withError = true;
      throw e;
    } finally {
      finish(withError);
    }
  }
}