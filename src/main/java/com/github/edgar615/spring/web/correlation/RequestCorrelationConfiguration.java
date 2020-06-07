package com.github.edgar615.spring.web.correlation;

import java.util.EnumSet;
import javax.servlet.DispatcherType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
@EnableConfigurationProperties(RequestCorrelationProperties.class)
public class RequestCorrelationConfiguration  {

  @Bean
  @ConditionalOnProperty(name = "web.correlation", havingValue = "uuid")
  public CorrelationIdGenerator uuidCorrelationFilter() {
    return new UuidGenerator();
  }

  @Bean
  @ConditionalOnProperty(name = "web.correlation", havingValue = "req_header")
  public CorrelationIdGenerator requestHeaderGenerator() {
    return new RequestHeaderGenerator();
  }

  @Bean
  @ConditionalOnBean(CorrelationIdGenerator.class)
  public RequestCorrelationFilter requestCorrelationFilter(CorrelationIdGenerator generator) {
    return new RequestCorrelationFilter(generator);
  }

  @Bean
  @ConditionalOnBean(RequestCorrelationFilter.class)
  public FilterRegistrationBean requestCorrelationFilterBean(
      RequestCorrelationFilter correlationFilter) {
    final FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
    filterRegistration.setFilter(correlationFilter);
    filterRegistration.setMatchAfter(false);
    filterRegistration.setDispatcherTypes(
        EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC));
    filterRegistration.setAsyncSupported(true);
    filterRegistration.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return filterRegistration;
  }

}
