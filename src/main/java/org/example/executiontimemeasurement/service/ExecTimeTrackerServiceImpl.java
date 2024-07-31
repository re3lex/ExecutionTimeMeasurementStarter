package org.example.executiontimemeasurement.service;

import lombok.extern.slf4j.Slf4j;

import java.util.Stack;

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


  private final ThreadLocal<Stack<Trace>> stack2 = ThreadLocal.withInitial(Stack::new);
  private final ThreadLocal<Trace> rootTrace = new ThreadLocal<>();

  @Override
  public void startTracking(String name) {
    Trace trace = new Trace(name);
    trace.setStart(System.currentTimeMillis());

    if (rootTrace.get() == null) {
      rootTrace.set(trace);
    }
    Stack<Trace> traces = stack2.get();
    if (!traces.empty()) {
      Trace parent = traces.peek();
      parent.getChildren().add(trace);
    }

    traces.add(trace);
  }

  @Override
  public void stopTracking(String name, boolean withError) {
    Stack<Trace> traces = stack2.get();
    if (traces.empty()) {
      log.error("There is no current trace!");
      return;
    }

    Trace trace = traces.pop();

    if (!trace.getName().equals(name)) {
      log.error("Name of current trace is not matching with provided name!");
      return;
    }

    trace.setEnd(System.currentTimeMillis());
    trace.setFailed(withError);

    long duration = trace.getEnd() - trace.getStart();
    log.trace("Executing {} took {}ms", trace.getName(), duration);
  }

  @Override
  public void clear() {
    stack2.remove();
    rootTrace.remove();
  }

  @Override
  public Trace getRootTrace() {
    return rootTrace.get();
  }

  @Override
  public String getStackAsAsciiTable() {
    StringBuilder sb = new StringBuilder("\n\n");
    sb.append(HEADER).append("\n");
    sb.append(DELIMITER).append("\n");

    addLine(sb, rootTrace.get(), 0);

    return sb.toString();
  }

  private void addLine(StringBuilder sb, Trace trace, int depth) {
    String pad = repeat(" ", depth * 2);
    String name = trace.getName();

    if (name.length() + pad.length() > TRACE_POINT_WIDTH) {
      name = right(trace.getName(), TRACE_POINT_WIDTH - pad.length());
    }

    String failedMark = trace.isFailed() ? HAS_ERROR_MARK : "";
    long duration = trace.getEnd() - trace.getStart();

    sb.append(TPL.formatted(pad + name, depth, duration, failedMark)).append("\n");

    for (Trace child : trace.getChildren()) {
      addLine(sb, child, depth + 1);
    }
  }


  @Override
  public String getStackAsCsv() {
    StringBuilder sb = new StringBuilder();
    sb.append(CSV_HEADER).append("\n");

    addCsvLine(sb, rootTrace.get(), 0);

    return sb.toString();
  }

  private void addCsvLine(StringBuilder sb, Trace trace, int depth) {
    long end = trace.getEnd();
    long start = trace.getStart();
    long duration = end - start;
    String failedMark = trace.isFailed() ? "true" : "";

    sb.append(CSV_ROW_TPL.formatted(depth, trace.getName(), start, end, duration, failedMark)).append("\n");

    for (Trace child : trace.getChildren()) {
      addCsvLine(sb, child, depth + 1);
    }
  }

  @Override
  public void printStack() {
    log.info(getStackAsAsciiTable());
  }

}
