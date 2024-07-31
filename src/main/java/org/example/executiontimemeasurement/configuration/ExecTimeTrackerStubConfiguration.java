package org.example.executiontimemeasurement.configuration;

import org.example.executiontimemeasurement.service.ExecutionTrackerService;
import org.example.executiontimemeasurement.service.ExecutionTrackerServiceStubImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingBean(ExecTimeTrackerConfiguration.class)
public class ExecTimeTrackerStubConfiguration extends BaseExecTimeTrackerConfiguration {

  @Bean
  public ExecutionTrackerService executionTrackerService() {
    return new ExecutionTrackerServiceStubImpl();
  }
}
