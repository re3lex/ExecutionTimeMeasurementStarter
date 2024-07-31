package org.example.executiontimemeasurement.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.executiontimemeasurement.annotation.TrackExecTime;
import org.example.executiontimemeasurement.configuration.properties.ExecTimeTrackerProperties;
import org.example.executiontimemeasurement.service.ExecTimeTrackerService;
import org.example.executiontimemeasurement.tracker.ExecTimeTrackerFactory;
import org.example.executiontimemeasurement.tracker.ThrowingSupplierTracker;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class ExecTimeTrackerAspect {
  private final ExecTimeTrackerFactory factory;
  private final ExecTimeTrackerService trackerService;
  private final ExecTimeTrackerProperties properties;
  private final ThreadLocal<Boolean> facedFirstAnnotation = ThreadLocal.withInitial(() -> false);

  @Around("@annotation(org.example.executiontimemeasurement.annotation.TrackExecTime)")
  public Object executionTime(ProceedingJoinPoint point) throws Throwable {
    TrackExecTime trackExecTime = getTrackExecTime(point);

    /*
     * Need to clear thread data and print trace at the end of execution into console if this is first faced TrackExecTime annotation.
     */
    boolean isFirst = false;
    if (!facedFirstAnnotation.get()) {
      trackerService.clear();
      facedFirstAnnotation.set(true);
      isFirst = true;
    }

    String traceName = trackExecTime.value();
    if (traceName == null || traceName.isEmpty()) {
      traceName = createTraceName(point);
    }

    ThrowingSupplierTracker<Object> supplierTracker = factory.throwingSupplierTracker(traceName);

    try {
      return supplierTracker.measure(point::proceed);
    } finally {
      if (isFirst) {
        if (properties.isPrintOnExit()) {
          String trace = getTrace();
          log.info("\n{}", trace);
        }
        facedFirstAnnotation.set(false);
        trackerService.clear();
      }
    }
  }

  private String getTrace() {
    String trace = "";
    ExecTimeTrackerProperties.PrintType printType = properties.getPrintType();
    if (printType == null) {
      printType = ExecTimeTrackerProperties.PrintType.TABLE;
    }

    if (printType == ExecTimeTrackerProperties.PrintType.TABLE || printType == ExecTimeTrackerProperties.PrintType.BOTH) {
      trace = trackerService.getStackAsAsciiTable() + "\n";
    }
    if (printType == ExecTimeTrackerProperties.PrintType.CSV || printType == ExecTimeTrackerProperties.PrintType.BOTH) {
      trace += trackerService.getStackAsCsv();
    }
    return trace;
  }

  private String createTraceName(ProceedingJoinPoint point) {
    MethodSignature methodSignature = (MethodSignature) point.getSignature();
    Class<?> implementationClass = point.getTarget().getClass();
    String methodName = methodSignature.getName();
    return implementationClass.getSimpleName() + "::" + methodName;
  }

  private TrackExecTime getTrackExecTime(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    TrackExecTime trackExecTime = method.getAnnotation(TrackExecTime.class);
    if (trackExecTime == null && method.getDeclaringClass().isInterface()) {
      String methodName = signature.getName();
      Class<?> implementationClass = joinPoint.getTarget().getClass();
      Method implementationMethod = implementationClass.getDeclaredMethod(methodName, method.getParameterTypes());
      trackExecTime = implementationMethod.getAnnotation(TrackExecTime.class);
    }
    return trackExecTime;
  }
}
