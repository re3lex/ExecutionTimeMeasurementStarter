package org.example.executiontimemeasurement.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "exec-time-tracker")
public class ExecTimeTrackerProperties {
  private boolean enabled;
}
