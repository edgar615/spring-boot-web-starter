package com.github.edgar615.spring.web.correlation;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;

/**
 * requestId的UUID生成器
 */
public class UuidGenerator implements CorrelationIdGenerator {

    /**
     * 使用UUID生成requestId.
     * @param request http请求
     * @return random uuid
     */
    @Override
    public String generate(HttpServletRequest request) {

        return UUID.randomUUID().toString();
    }
}
