package com.github.edgar615.spring.web;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * Created by Edgar on 2017/11/16.
 *
 * @author Edgar  Date 2017/11/16
 */
public class InterceptorConfig {

  private List<String> exclude;

  public List<String> getExclude() {
    return exclude;
  }

  public void setExclude(List<String> exclude) {
    this.exclude = exclude;
  }
}
