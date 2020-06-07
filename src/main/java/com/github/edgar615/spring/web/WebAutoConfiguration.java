package com.github.edgar615.spring.web;

import com.github.edgar615.spring.web.log.RequestLoggerFilter;
import com.github.edgar615.spring.web.log.WebLogConfig;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2017/10/7.
 *
 * @ConditionalOnBean：当容器里有指定的Bean 的条件下。
 * @ConditionalOnClass：当类路径下有指定的类的条件下。
 * @ConditionalOnExpression：基于SpEL 表达式作为判断条件。
 * @ConditionalOnJava：基于JVM 版本作为判断条件。
 * @ConditionalOnJndi：在JNDI 存在的条件下查找指定的位置。
 * @ConditionalOnMissingBean：当容器里没有指定Bean 的情况下。
 * @ConditionalOnMissingClass：当类路径下没有指定的类的条件下。
 * @ConditionalOnNotWebApplication：当前项目不是Web 项目的条件下。
 * @ConditionalOnProperty：指定的属性是否有指定的值。
 * @ConditionalOnResource：类路径是否有指定的值。
 * @ConditionalOnSingleCandidate：当指定Bean 在容器中只有一个，或者虽然有多个但是指定首选的Bean。
 * @ConditionalOnWebApplication：当前项目是Web 项目的条件下。
 */
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties({WebProperties.class})
public class WebAutoConfiguration {

  @Bean
  public RequestLoggerFilter loggerFilter(WebProperties webProperties) {
    RequestLoggerFilter filter = new RequestLoggerFilter(formatter);
    if (webProperties.getLogConfig() == null) {
      return filter;
    }
    WebLogConfig webLogConfig = webProperties.getLogConfig();
    webLogConfig.setShowErrorStackTrace(webLogConfig.isShowErrorStackTrace());
    webLogConfig.setShowReqBody(webLogConfig.isShowReqBody());

    if (webLogConfig.getIgnoreLogPath() == null) {
      return filter;
    }
    List<String> ignoreList = webProperties.getLogConfig().getIgnoreLogPath();
    for (String ignore : ignoreList) {
      filter.addIgnorePrefix(ignore);
    }
    return filter;
  }

}
