package org.example.executiontimemeasurement.service;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.repeat;
import static org.apache.commons.lang3.StringUtils.right;

@Slf4j
public class ExecTimeTrackerServiceImpl implements ExecTimeTrackerService {
  private static final int TRACE_POINT_WIDTH = 80;
  private static final int DEPTH_WIDTH = 5;
  private static final int DURATION_WIDTH = 12;
  private static final int HAS_ERROR_WIDTH = 16;

  // ASCII table
  private static final String TPL = "|%-" + TRACE_POINT_WIDTH + "s | %" + DEPTH_WIDTH + "s | %" + DURATION_WIDTH + "s | %" + HAS_ERROR_WIDTH + "s|";
  private static final String DELIMITER = TPL.formatted(repeat("-", TRACE_POINT_WIDTH), repeat("-", DEPTH_WIDTH), repeat("-", DURATION_WIDTH), repeat("-", HAS_ERROR_WIDTH));
  private static final String HEADER = TPL.formatted("Trace point name", "Depth", "Duration, ms", "Exception thrown");
  private static final String HAS_ERROR_MARK = "V" + repeat(" ", (HAS_ERROR_WIDTH / 2));

  // CSV
  private static final String CSV_HEADER = "Depth;Trace point;Start;End;Duration, ms;Exception";
  private static final String CSV_ROW_TPL = "%s;\"%s\";%s;%s;%s;%s";

  private final ThreadLocal<Integer> depth = ThreadLocal.withInitial(() -> 0);
  private final ThreadLocal<List<Trace>> stack = ThreadLocal.withInitial(ArrayList::new);

  @Override
  public Trace startTracking(String name) {
    int currentDepth = depth.get();
    Trace trace = new Trace(currentDepth, name);
    trace.setStart(System.currentTimeMillis());
    stack.get().add(trace);
    increaseDepth();
    return trace;
  }

  @Override
  public void stopTracking(Trace trace) {
    if (trace == null) {
      log.error("Trace is not provided");
      return;
    }

    trace.setEnd(System.currentTimeMillis());
    long duration = trace.getEnd() - trace.getStart();
    log.trace("Depth {}. Executing {} took {}ms", trace.getDepth(), trace.getName(), duration);
    decreaseDepth();
  }

  @Override
  public void clear() {
    stack.get().clear();
    depth.set(0);
  }

  @Override
  public int getDepth() {
    return depth.get();
  }

  @Override
  public List<Trace> getStack() {
    return stack.get();
  }

  @Override
  public String getStackAsAsciiTable() {
    List<ExecTimeTrackerServiceImpl.Trace> stack = getStack();

    StringBuilder sb = new StringBuilder();
    sb.append(HEADER).append("\n");
    sb.append(DELIMITER).append("\n");

    for (ExecTimeTrackerServiceImpl.Trace trace : stack) {
      int depth = trace.getDepth();
      String pad = repeat(" ", depth * 2);
      String name = trace.getName();

      if (name.length() + pad.length() > TRACE_POINT_WIDTH) {
        name = right(trace.getName(), TRACE_POINT_WIDTH - pad.length());
      }

      String failedMark = trace.isFailed() ? HAS_ERROR_MARK : "";
      long duration = trace.getEnd() - trace.getStart();

      sb.append(TPL.formatted(pad + name, depth, duration, failedMark)).append("\n");
    }

    return sb.toString();
  }

  @Override
  public String getStackAsCsv() {
    StringBuilder sb = new StringBuilder();
    sb.append(CSV_HEADER).append("\n");

    List<ExecTimeTrackerServiceImpl.Trace> stack = getStack();
    for (ExecTimeTrackerServiceImpl.Trace trace : stack) {
      int depth = trace.getDepth();
      long end = trace.getEnd();
      long start = trace.getStart();
      long duration = end - start;
      String failedMark = trace.isFailed() ? "true" : "";

      sb.append(CSV_ROW_TPL.formatted(depth, trace.getName(), start, end, duration, failedMark)).append("\n");
    }
    return sb.toString();
  }

  @Override
  public void printStack() {
    log.info(getStackAsAsciiTable());
  }

  private void increaseDepth() {
    depth.set(depth.get() + 1);
  }

  private void decreaseDepth() {
    Integer currentDepth = depth.get();
    if (currentDepth == 0) {
      log.error("Depth is already zero. Cannot be decreased");
    } else {
      depth.set(currentDepth - 1);
    }
  }
}
