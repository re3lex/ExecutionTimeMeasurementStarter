package org.example.executiontimemeasurement.configuration;

import org.example.executiontimemeasurement.configuration.properties.ExecTimeTrackerProperties;
import org.example.executiontimemeasurement.service.ExecTimeTrackerService;
import org.example.executiontimemeasurement.tracker.ExecTimeTrackerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(ExecTimeTrackerProperties.class)
abstract class BaseExecTimeTrackerConfiguration {

  public abstract ExecTimeTrackerService executionTrackerService();

  @Bean
  public ExecTimeTrackerFactory executionTrackerFactory(ExecTimeTrackerService trackerService) {
    return new ExecTimeTrackerFactory(trackerService);
  }
}
