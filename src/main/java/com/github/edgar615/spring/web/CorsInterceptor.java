package com.github.edgar615.spring.web;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsProcessor;
import org.springframework.web.cors.DefaultCorsProcessor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器抛出异常之后，会跳过cors，所以使用一个自定义的CORS，放在所有拦截器之前执行
 */
public class CorsInterceptor extends HandlerInterceptorAdapter {

  private final CorsConfiguration config;

  private CorsProcessor corsProcessor = new DefaultCorsProcessor();

  public CorsInterceptor(CorsConfiguration config) {
    this.config = config;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
          throws Exception {

    return corsProcessor.processRequest(this.config, request, response);
  }
}
