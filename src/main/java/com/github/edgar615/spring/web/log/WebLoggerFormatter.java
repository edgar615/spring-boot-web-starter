package com.github.edgar615.spring.web.log;

public interface WebLoggerFormatter {

    void logRequest(RequestLogContext requestLogContext);

    void logResponse(ResponseLogContext responseLogContext);
}
