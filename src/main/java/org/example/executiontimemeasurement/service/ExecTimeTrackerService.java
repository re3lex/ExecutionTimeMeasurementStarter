package org.example.executiontimemeasurement.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface ExecTimeTrackerService {

  default void startTracking(String name) {
  }

  default void stopTracking(String name, boolean withError) {

  }

  default int getDepth() {
    return -1;
  }

  default void clear() {

  }

  default Trace getRootTrace() {
    return null;
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
    private final String name;
    private long start;
    private long end;
    private boolean failed;

    private List<Trace> children = new ArrayList<>();
  }
}
