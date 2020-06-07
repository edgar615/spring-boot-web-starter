package com.github.edgar615.spring.web.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Request
 *
 * <pre>
 * Incoming Request: 2d66e4bc-9a0d-11e5-a84c-1f39510f0d6b
 * GET http://example.org/test HTTP/1.1
 * Accept: application/json
 * Host: localhost
 * Content-Type: text/plain
 *
 * Hello world!
 * </pre>
 *
 * Response
 *
 * <pre>
 * Outgoing Response: 2d66e4bc-9a0d-11e5-a84c-1f39510f0d6b
 * Duration: 25 ms
 * HTTP/1.1 200
 * Content-Type: application/json
 *
 * {"value":"Hello world!"}
 * </pre>
 */
public class HttpWebLoggerFormatter implements WebLoggerFormatter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggerFilter.class);

    @Override
    public void logRequest(RequestLogContext requestLogContext) {
        LOGGER.info("Incoming Request: {}\n" +
                "{} {}", requestLogContext.getId(),
                requestLogContext.getMethod(), requestLogContext.getUri());
    }

    @Override
    public void logResponse(ResponseLogContext responseLogContext) {

    }
}
