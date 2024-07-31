package com.re3lex.executiontimemeasurement.tracker;

import lombok.extern.slf4j.Slf4j;
import com.re3lex.executiontimemeasurement.service.ExecTimeTrackerService;

@Slf4j
public abstract class AbstractExecutionTracker {

  protected final String name;
  private final ExecTimeTrackerService trackerService;
  private ExecTimeTrackerService.Trace trace;

  public AbstractExecutionTracker(String name, ExecTimeTrackerService trackerService) {
    this.name = name;
    this.trackerService = trackerService;
  }

  protected void start() {
    this.trace = trackerService.startTracking(name);
  }

  protected void finish(boolean withError) {
    trace.setFailed(withError);
    trackerService.stopTracking(trace);
  }
}