package com.github.edgar615.spring.web.log;

import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.Map;

public interface LogContext {

    String EMPTY = "-";

    Map<String, Object> EMPTY_MAP = new HashMap<>();

    default Map<String, Object> getOrDefault(Map<String, Object> value) {
        if (value == null) {
            return EMPTY_MAP;
        }
        return value;
    }

    default String getOrDefault(String value) {
        if (Strings.isNullOrEmpty(value)) {
            return EMPTY;
        }
        return value;
    }

    default Long getOrDefault(Long value) {
        return value == null ? 0 : value;
    }
}
