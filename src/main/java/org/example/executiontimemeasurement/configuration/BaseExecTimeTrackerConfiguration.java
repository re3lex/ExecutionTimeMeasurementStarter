package org.example.executiontimemeasurement.configuration;

import org.example.executiontimemeasurement.configuration.properties.ExecTimeTrackerProperties;
import org.example.executiontimemeasurement.service.ExecutionTrackerService;
import org.example.executiontimemeasurement.tracker.ExecutionTrackerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(ExecTimeTrackerProperties.class)
abstract class BaseExecTimeTrackerConfiguration {

  public abstract ExecutionTrackerService executionTrackerService();

  @Bean
  public ExecutionTrackerFactory executionTrackerFactory(ExecutionTrackerService trackerService) {
    return new ExecutionTrackerFactory(trackerService);
  }
}
