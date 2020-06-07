package com.github.edgar615.spring.web;

import com.github.edgar615.spring.web.log.WebLogConfig;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * Created by Edgar on 2017/11/16.
 *
 * @author Edgar  Date 2017/11/16
 */
@ConfigurationProperties(prefix = "web")
@Service
public class WebProperties {

  private WebLogConfig logConfig;

  private Map<String, InterceptorConfig> interceptors;

  public Map<String, InterceptorConfig> getInterceptors() {
    return interceptors;
  }

  public void setInterceptors(
      Map<String, InterceptorConfig> interceptors) {
    this.interceptors = interceptors;
  }

  public WebLogConfig getLogConfig() {
    return logConfig;
  }

  public void setLogConfig(WebLogConfig logConfig) {
    this.logConfig = logConfig;
  }
}
