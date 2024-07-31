package com.re3lex.executiontimemeasurement.configuration;

import com.re3lex.executiontimemeasurement.service.ExecTimeTrackerService;
import com.re3lex.executiontimemeasurement.service.ExecTimeTrackerServiceStubImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingBean(ExecTimeTrackerConfiguration.class)
public class ExecTimeTrackerStubConfiguration extends BaseExecTimeTrackerConfiguration {

  @Bean
  public ExecTimeTrackerService executionTrackerService() {
    return new ExecTimeTrackerServiceStubImpl();
  }
}
