package org.example.executiontimemeasurement.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface ExecutionTrackerService {

  default Trace startTracking(String name) {
    return null;
  }

  default void stopTracking(Trace trace) {

  }

  default void clear() {

  }

  default List<Trace> getStack() {
    return Collections.emptyList();
  }

  default String getStackAsString() {
    return null;
  }

  default String getStackAsCsv() {
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
    private long start;
    private long end;
    private boolean failed;
  }
}
