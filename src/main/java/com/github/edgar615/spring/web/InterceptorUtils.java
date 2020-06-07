package com.github.edgar615.spring.web;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

public class InterceptorUtils {

  public static void addInterceptors(InterceptorRegistry registry,
      HandlerInterceptor interceptor, WebProperties webProperties,
      int order) {
    String[] patterns = patterns(interceptor, webProperties);
    registry.addInterceptor(interceptor).addPathPatterns("/**")
        .excludePathPatterns(patterns).order(order);
  }

  private static String[] patterns(HandlerInterceptor interceptor, WebProperties webProperties) {
    if (webProperties.getInterceptors() == null) {
      return new String[]{};
    }
    String interceptorName = interceptor.getClass().getSimpleName();
    InterceptorConfig interceptorConfig = webProperties.getInterceptors().get(interceptorName);
    if (interceptorConfig == null) {
      return new String[]{};
    }
    List<String> excludePathPatternsForAuth = new ArrayList<>();
    if (interceptorConfig.getExclude() != null) {
      excludePathPatternsForAuth.addAll(interceptorConfig.getExclude());
    }
    excludePathPatternsForAuth.add("/error");
    return excludePathPatternsForAuth.toArray(new String[excludePathPatternsForAuth.size()]);
  }

}
