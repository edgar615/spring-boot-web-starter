package com.github.edgar615.spring.web.correlation;

import javax.servlet.http.HttpServletRequest;

/**
 * requestId的生成接口
 */
public interface CorrelationIdGenerator {

    /**
     * 生成requestId.
     *
     * @return request ID
     */
    String generate(HttpServletRequest request);
}
