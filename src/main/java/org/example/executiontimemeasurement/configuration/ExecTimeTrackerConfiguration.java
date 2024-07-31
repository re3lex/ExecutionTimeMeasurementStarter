package org.example.executiontimemeasurement.configuration;

import org.example.executiontimemeasurement.advice.ExecTimeTrackerAdvice;
import org.example.executiontimemeasurement.configuration.properties.ExecTimeTrackerProperties;
import org.example.executiontimemeasurement.service.ExecutionTrackerService;
import org.example.executiontimemeasurement.service.ExecutionTrackerServiceImpl;
import org.example.executiontimemeasurement.tracker.ExecutionTrackerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "exec-time-tracker.enabled", havingValue = "true")
public class ExecTimeTrackerConfiguration extends BaseExecTimeTrackerConfiguration {

  @Bean
  public ExecTimeTrackerAdvice execTimeTrackerAdvice(ExecutionTrackerFactory executionTrackerFactory) {
    return new ExecTimeTrackerAdvice(executionTrackerFactory);
  }

  @Bean
  public ExecutionTrackerService executionTrackerService() {
    return new ExecutionTrackerServiceImpl();
  }
}
