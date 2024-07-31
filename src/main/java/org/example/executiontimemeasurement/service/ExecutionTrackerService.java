package org.example.executiontimemeasurement.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

public interface ExecutionTrackerService {

  default int startTracking(String name) {
    return 0;
  }

  default void stopTracking(int idx, long duration, boolean withError) {

  }

  default void clear() {

  }

  default List<Trace> getStack() {
    return Collections.emptyList();
  }

  default String getStackAsString() {
    return null;
  }

  default void printStack() {

  }

  @Getter
  @Setter
  @RequiredArgsConstructor
  class Trace {
    private final int depth;
    private final String name;
    private long duration;
    private boolean failed;
  }
}
