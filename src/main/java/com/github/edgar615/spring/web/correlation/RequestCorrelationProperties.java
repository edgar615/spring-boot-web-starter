package com.github.edgar615.spring.web.correlation;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "web")
public class RequestCorrelationProperties {

  private String correlation;

  public String getCorrelation() {
    return correlation;
  }

  public void setCorrelation(String correlation) {
    this.correlation = correlation;
  }
}
