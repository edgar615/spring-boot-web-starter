package com.github.edgar615.spring.web.correlation;

import com.github.edgar615.spring.web.WebConsts;
import com.google.common.base.Strings;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * requestId生成的的Filter，应该在所有Filter之前执行
 */
public class RequestCorrelationFilter implements Filter {

  private final CorrelationIdGenerator correlationIdGenerator;

  private final CorrelationIdGenerator defaultCorrelationIdGenerator;

  public RequestCorrelationFilter(
      CorrelationIdGenerator correlationIdGenerator) {
    this.correlationIdGenerator = correlationIdGenerator;
    this.defaultCorrelationIdGenerator = new UuidGenerator();
  }


  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
      HttpServletRequest httpServletRequest = (HttpServletRequest) request;
      String correlationId = correlationIdGenerator.generate(httpServletRequest);
      if (Strings.isNullOrEmpty(correlationId)) {
        correlationId = defaultCorrelationIdGenerator.generate(httpServletRequest);
      }
      RequestCorrelationHolder.set(correlationId);
      chain.doFilter(request, response);
    } else {
      chain.doFilter(request, response);
    }
  }

  @Override
  public void destroy() {
    RequestCorrelationHolder.clear();
  }

  private String getCorrelationId(HttpServletRequest request) {
    return request.getHeader(WebConsts.HEADER_NAME);
  }
}
