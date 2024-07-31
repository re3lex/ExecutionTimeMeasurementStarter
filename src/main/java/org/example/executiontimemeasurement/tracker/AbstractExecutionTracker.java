package org.example.executiontimemeasurement.tracker;

import lombok.extern.slf4j.Slf4j;
import org.example.executiontimemeasurement.service.ExecutionTrackerService;
import org.springframework.util.StopWatch;

@Slf4j
public abstract class AbstractExecutionTracker {

  protected final String name;
  private final ExecutionTrackerService trackerService;
  private int index;

  public AbstractExecutionTracker(String name, ExecutionTrackerService trackerService) {
    this.name = name;
    this.trackerService = trackerService;
  }

  protected StopWatch start() {
    final StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    this.index = trackerService.startTracking(name);
    return stopWatch;
  }

  protected void finish(final StopWatch stopWatch, boolean withError) {
    stopWatch.stop();
    long duration = stopWatch.getLastTaskTimeMillis();
    trackerService.stopTracking(index, duration, withError);
  }

}