package com.github.edgar615.spring.web.log;

import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoggingHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final String id;
    private final String body;
    private final Long receiveTime;

    public LoggingHttpServletRequestWrapper(String id, HttpServletRequest request) throws IOException {
        super(request);
        this.id = id;
        this.body = CharStreams.toString(new InputStreamReader(request.getInputStream()));
        this.receiveTime = System.currentTimeMillis();
    }

    public String getClientIp() {
        String ip = getHeader("X-Forwarded-For");
        if (!Strings.isNullOrEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = getHeader("X-Real-IP");
        if (!Strings.isNullOrEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return getRemoteHost();
    }

    public Long getReceiveTime() {
        return receiveTime;
    }

    public Map<String, Object> getQueryMap() {
        return queryMap();
    }

    public String getBody() {
        return body;
    }

    public Map<String, Object> getHeaderMap() {
        return headerMap();
    }

    public String getRequestBody() {
        return body;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(body.getBytes());
        return new LoggingHttpServletRequestWrapper.ResettableServletInputStream(in, super.getInputStream());
    }

    private Map<String, Object> queryMap() {
        Map<String, Object> paramMap = new HashMap<>();
        Enumeration<String> names = getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            List<String> valueList = Lists.newArrayList(getParameterValues(name));
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

    private Map<String, Object> headerMap() {
        Map<String, Object> headerMap = new HashMap<>();
        Enumeration<String> headers = getHeaderNames();
        while (headers.hasMoreElements()) {
            String name = headers.nextElement();
            Enumeration<String> value = getHeaders(name);
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

    private final class ResettableServletInputStream extends ServletInputStream {
        private final ServletInputStream inputStream;

        private ByteArrayInputStream bais;

        public ResettableServletInputStream(ByteArrayInputStream bais, ServletInputStream inputStream) {
            this.bais = bais;
            this.inputStream = inputStream;
        }

        @Override
        public int available() {
            return this.bais.available();
        }

        @Override
        public int read() {
            return this.bais.read();
        }

        @Override
        public int read(byte[] buf, int off, int len) {
            return this.bais.read(buf, off, len);
        }

        @Override
        public boolean isFinished() {
            return inputStream.isFinished();
        }

        @Override
        public boolean isReady() {
            return inputStream.isReady();
        }

        @Override
        public void setReadListener(ReadListener listener) {
            inputStream.setReadListener(listener);
        }
    }
}
