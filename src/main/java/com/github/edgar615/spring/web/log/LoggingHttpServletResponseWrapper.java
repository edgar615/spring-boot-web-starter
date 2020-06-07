package com.github.edgar615.spring.web.log;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class LoggingHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private final String id;

    private final long receiveTime;

    private final long sendTime;

    private final long duration;

    public LoggingHttpServletResponseWrapper(String id, long receiveTime,HttpServletResponse response) {
        super(response);
        this.id = id;
        this.receiveTime = receiveTime;
        this.sendTime = System.currentTimeMillis();
        this.duration = this.sendTime - this.receiveTime;
    }
}
