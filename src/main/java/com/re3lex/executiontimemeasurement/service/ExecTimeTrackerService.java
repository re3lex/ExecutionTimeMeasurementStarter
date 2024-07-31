package com.re3lex.executiontimemeasurement.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

public interface ExecTimeTrackerService {

  default Trace startTracking(String name) {
    return null;
  }

  default void stopTracking(Trace trace) {

  }

  default int getDepth() {
    return -1;
  }

  default void clear() {

  }

  default List<Trace> getStack() {
    return Collections.emptyList();
  }

  default String getStackAsAsciiTable() {
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
