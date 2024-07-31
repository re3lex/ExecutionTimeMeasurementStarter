package org.example.executiontimemeasurement.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.executiontimemeasurement.annotation.TrackExecTime;
import org.example.executiontimemeasurement.tracker.ExecutionTrackerFactory;
import org.example.executiontimemeasurement.tracker.ThrowingSupplierTracker;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class ExecTimeTrackerAdvice {
  private final ExecutionTrackerFactory factory;
  private final Map<String, ThrowingSupplierTracker<Object>> store = new ConcurrentHashMap<>();

  @Around("@annotation(org.example.executiontimemeasurement.annotation.TrackExecTime)")
  public Object executionTime(ProceedingJoinPoint point) throws Throwable {
    TrackExecTime trackExecTime = getTrackExecTime(point);
    String traceName = trackExecTime.value();
    if (traceName == null || traceName.isEmpty()) {
      traceName = createTraceName(point);
    }

    ThrowingSupplierTracker<Object> supplierTracker = store.computeIfAbsent(traceName, factory::throwingSupplierTracker);

    return supplierTracker.measure(point::proceed);
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
