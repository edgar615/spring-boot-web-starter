package com.github.edgar615.spring.web.exception;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "web")
public class ErrorStatusProperties {
    private Map<Integer, Integer> error;

    public Map<Integer, Integer> getError() {
        return error;
    }

    public void setError(Map<Integer, Integer> error) {
        this.error = error;
    }
}
