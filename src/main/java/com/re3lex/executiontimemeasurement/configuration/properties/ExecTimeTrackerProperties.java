package com.re3lex.executiontimemeasurement.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "exec-time-tracker")
public class ExecTimeTrackerProperties {
  private boolean enabled;
  private boolean printOnExit;
  private PrintType printType = PrintType.TABLE;

  public enum PrintType {
    TABLE, CSV, BOTH
  }
}
