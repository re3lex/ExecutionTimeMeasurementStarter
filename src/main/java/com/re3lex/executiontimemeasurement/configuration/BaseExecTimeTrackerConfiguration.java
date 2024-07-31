package com.re3lex.executiontimemeasurement.configuration;

import com.re3lex.executiontimemeasurement.configuration.properties.ExecTimeTrackerProperties;
import com.re3lex.executiontimemeasurement.service.ExecTimeTrackerService;
import com.re3lex.executiontimemeasurement.tracker.ExecTimeTrackerFactory;
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
