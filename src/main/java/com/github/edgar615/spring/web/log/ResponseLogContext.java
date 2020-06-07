package com.github.edgar615.spring.web.log;

import java.util.Map;

public class ResponseLogContext implements LogContext {

    private final String HEADER_DIRECTIVE = "H";

    private final String BODY_DIRECTIVE = "B";

    private final String BODY_LEN_DIRECTIVE = "b";

    private final String DURATION_DIRECTIVE = "d";

    private final String SEND_TIME_DIRECTIVE = "t";

    private String id;

    private int status;

    private String body;

    private Map<String, Object> header;

    private Long sendTime;

    private Long duration;

    /**
     * 根据指令转换为值，这里没有做太复杂的模式，直接用if/else实现
     * @param percentDirective
     * @return
     */
    public Object get(String percentDirective) {
        if (HEADER_DIRECTIVE.equals(percentDirective)) {
            return getOrDefault(header);
        }
        if (SEND_TIME_DIRECTIVE.equals(percentDirective)) {
            return getOrDefault(sendTime);
        }
        if (DURATION_DIRECTIVE.equals(percentDirective)) {
            return getOrDefault(duration);
        }
        if (BODY_DIRECTIVE.equals(percentDirective)) {
            return getOrDefault(body);
        }
        if (BODY_LEN_DIRECTIVE.equals(percentDirective)) {
            if (body == null) {
                return 0;
            }
            return body.getBytes().length;
        }
        return EMPTY;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, Object> getHeader() {
        return header;
    }

    public void setHeader(Map<String, Object> header) {
        this.header = header;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
