package com.github.edgar615.spring.web.correlation;

import com.google.common.base.Strings;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * requestId的UUID生成器
 */
public class RequestHeaderGenerator implements CorrelationIdGenerator {

    /**
     * 使用UUID生成requestId.
     * @param request http请求
     * @return random uuid
     */
    @Override
    public String generate(HttpServletRequest request) {
       String id = request.getHeader(RequestCorrelationConsts.HEADER_NAME);
       if (Strings.isNullOrEmpty(id)) {
           return new UuidGenerator().generate(request);
       }
       return id;
    }
}
