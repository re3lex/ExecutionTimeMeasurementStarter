package com.re3lex.executiontimemeasurement.configuration;

import com.re3lex.executiontimemeasurement.aop.ExecTimeTrackerAspect;
import com.re3lex.executiontimemeasurement.configuration.properties.ExecTimeTrackerProperties;
import com.re3lex.executiontimemeasurement.service.ExecTimeTrackerService;
import com.re3lex.executiontimemeasurement.service.ExecTimeTrackerServiceImpl;
import com.re3lex.executiontimemeasurement.tracker.ExecTimeTrackerFactory;
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
