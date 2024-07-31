package org.example.executiontimemeasurement.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.repeat;
import static org.apache.commons.lang3.StringUtils.right;

@Slf4j
public class ExecutionTrackerServiceImpl implements ExecutionTrackerService {
  private static final int TRACE_POINT_WIDTH = 80;
  private static final int DEPTH_WIDTH = 5;
  private static final int DURATION_WIDTH = 12;
  private static final int HAS_ERROR_WIDTH = 16;

  private static final String HAS_ERROR_MARK = "V" + repeat(" ", (HAS_ERROR_WIDTH / 2));
  private static final String TPL = "|%-" + TRACE_POINT_WIDTH + "s | %" + DEPTH_WIDTH + "s | %" + DURATION_WIDTH + "s | %" + HAS_ERROR_WIDTH + "s|";
  private static final String DELIMITER = TPL.formatted(repeat("-", TRACE_POINT_WIDTH), repeat("-", DEPTH_WIDTH), repeat("-", DURATION_WIDTH), repeat("-", HAS_ERROR_WIDTH));
  private static final String HEADER = TPL.formatted("Trace point name", "Depth", "Duration, ms", "Exception thrown");

  private final ThreadLocal<Integer> depth = ThreadLocal.withInitial(() -> 0);
  private final ThreadLocal<List<Trace>> stack = ThreadLocal.withInitial(ArrayList::new);

  @Override
  public int startTracking(String name) {
    int currentDepth = depth.get();
    stack.get().add(new Trace(currentDepth, name));
    increaseDepth();
    return stack.get().size() - 1;
  }

  @Override
  public void stopTracking(int idx, long duration, boolean withError) {
    Trace trace = stack.get().get(idx);
    if (trace == null) {
      log.error("No trace found for id {}", idx);
      return;
    }

    trace.setDuration(duration);
    trace.setFailed(withError);
    log.trace("Depth {}. Executing {} took {}ms", trace.getDepth(), trace.getName(), duration);
    decreaseDepth();
  }

  @Override
  public void clear() {
    stack.get().clear();
    depth.set(0);
  }

  @Override
  public List<Trace> getStack() {
    return stack.get();
  }

  @Override
  public String getStackAsString() {
    List<ExecutionTrackerServiceImpl.Trace> stack = getStack();

    StringBuilder sb = new StringBuilder("\n\n");
    sb.append(HEADER).append("\n");
    sb.append(DELIMITER).append("\n");

    for (ExecutionTrackerServiceImpl.Trace trace : stack) {
      int depth = trace.getDepth();
      String pad = repeat(" ", depth * 2);
      String name = trace.getName();

      if (name.length() + pad.length() > TRACE_POINT_WIDTH) {
        name = right(trace.getName(), TRACE_POINT_WIDTH - pad.length());
      }

      String failedMark = trace.isFailed() ? HAS_ERROR_MARK : "";

      sb.append(TPL.formatted(pad + name, depth, trace.getDuration(), failedMark)).append("\n");
    }

    return sb.toString();
  }

  @Override
  public void printStack() {
    log.info(getStackAsString());
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
