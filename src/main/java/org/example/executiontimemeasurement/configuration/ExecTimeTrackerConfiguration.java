package org.example.executiontimemeasurement.configuration;

import org.example.executiontimemeasurement.aop.ExecTimeTrackerAspect;
import org.example.executiontimemeasurement.configuration.properties.ExecTimeTrackerProperties;
import org.example.executiontimemeasurement.service.ExecTimeTrackerService;
import org.example.executiontimemeasurement.service.ExecTimeTrackerServiceImpl;
import org.example.executiontimemeasurement.tracker.ExecTimeTrackerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "exec-time-tracker.enabled", havingValue = "true")
public class ExecTimeTrackerConfiguration extends BaseExecTimeTrackerConfiguration {

  @Bean
  public ExecTimeTrackerAspect execTimeTrackerAspect(ExecTimeTrackerFactory execTimeTrackerFactory, ExecTimeTrackerService trackerService, ExecTimeTrackerProperties properties) {
    return new ExecTimeTrackerAspect(execTimeTrackerFactory, trackerService, properties);
  }

  @Bean
  public ExecTimeTrackerService executionTrackerService() {
    return new ExecTimeTrackerServiceImpl();
  }
}
