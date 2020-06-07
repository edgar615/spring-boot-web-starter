package com.github.edgar615.spring.web.log;

import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class LogUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggerFilter.class);

    public static RequestLogContext createRequestContext(String id, ResettableStreamRequestWrapper request) {
        RequestLogContext requestLogContext = new RequestLogContext();
        requestLogContext.setId(id);
        requestLogContext.setClientIp(getClientIp(request));
        requestLogContext.setReceiveTime(System.currentTimeMillis());
        requestLogContext.setMethod(request.getMethod());
        requestLogContext.setHeader(headerMap(request));
        requestLogContext.setQuery(queryMap(request));
        requestLogContext.setBody(body(request));
        requestLogContext.setUri(request.getContextPath());
        return requestLogContext;
    }

    public static ResponseLogContext createResponseContext(String id, long receiveTime, ContentCachingResponseWrapper wrappedResponse) {
        ResponseLogContext responseLogContext = new ResponseLogContext();
        responseLogContext.setId(id);
        responseLogContext.setStatus(wrappedResponse.getStatus());
        responseLogContext.setSendTime(System.currentTimeMillis());
        responseLogContext.setDuration(responseLogContext.getSendTime() - receiveTime);
        responseLogContext.setHeader(headerMap(wrappedResponse));
        responseLogContext.setBody(new String(wrappedResponse.getContentAsByteArray()));
        return responseLogContext;
    }

    private static boolean hasBody(HttpServletRequest request) {
        String method = request.getMethod();
        return method.equalsIgnoreCase("post")
                || method.equalsIgnoreCase("put")
                || method.equalsIgnoreCase("delete");
    }

    private static String body(ResettableStreamRequestWrapper resettableStreamRequestWrapper) {
        return resettableStreamRequestWrapper.getRequestBody();
    }

    @Deprecated
    private static String body(HttpServletRequest request) {
        if (!hasBody(request)) {
            return null;
        }
        ContentCachingRequestWrapper wrapper = WebUtils
                .getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper == null) {
            return null;
        }
        byte[] buf = wrapper.getContentAsByteArray();
        if (buf.length > 0) {
            String payload;
            try {
                payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
            } catch (UnsupportedEncodingException ex) {
                payload = "unknown";
            }

            return payload;
        }
        return null;
    }

    private static Map<String, Object> queryMap(HttpServletRequest request) {
        Map<String, Object> paramMap = new HashMap<>();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            List<String> valueList = Lists.newArrayList(request.getParameterValues(name));
            if (valueList.isEmpty()) {
                // ignore
            } else if (valueList.size() == 1) {
                paramMap.put(name, valueList.get(0));
            } else {
                paramMap.put(name, valueList);
            }
        }
        return paramMap;
    }

    private static Map<String, Object> headerMap(HttpServletRequest request) {
        Map<String, Object> headerMap = new HashMap<>();
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String name = headers.nextElement();
            Enumeration<String> value = request.getHeaders(name);
            List<String> valueList = Lists.newArrayList(Iterators.forEnumeration(value));
            if (valueList.isEmpty()) {
                // ignore
            } else if (valueList.size() == 1) {
                headerMap.put(name, valueList.get(0));
            } else {
                headerMap.put(name, valueList);
            }
        }
        return headerMap;
    }

    private static Map<String, Object> headerMap(HttpServletResponse response) {
        Map<String, Object> headerMap = new HashMap<>();
        Collection<String> headers = response.getHeaderNames();
        for (String name : headers) {
            List<String> valueList = new ArrayList<>(response.getHeaders(name));
            if (valueList.isEmpty()) {
                // ignore
            } else if (valueList.size() == 1) {
                headerMap.put(name, valueList.get(0));
            } else {
                headerMap.put(name, valueList);
            }
        }
        return headerMap;
    }

    private static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (!Strings.isNullOrEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (!Strings.isNullOrEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteHost();
    }

    public static void logReceviced(RequestLogContext requestLogContext, WebLoggerFormatter formatter) {

    }

    public static void logSend(ResponseLogContext responseLogContext, WebLoggerFormatter formatter) {

    }
}
