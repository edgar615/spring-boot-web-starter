package com.github.edgar615.spring.web.log;

import com.github.edgar615.spring.web.correlation.RequestCorrelationHolder;
import com.google.common.base.Strings;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

/**
 * Doogies very cool HTTP request logging
 * <p>
 * There is also {@link org.springframework.web.filter.CommonsRequestLoggingFilter}  but it cannot
 * log request method And it cannot easily be extended.
 * <p>
 * https://mdeinum.wordpress.com/2015/07/01/spring-framework-hidden-gems/
 * http://stackoverflow.com/questions/8933054/how-to-read-and-copy-the-http-servlet-response
 * -output-stream-content-for-logging
 */
public class RequestLoggerFilter extends OncePerRequestFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggerFilter.class);

//  private boolean includeResponsePayload = true;

  private final List<String> ignorePrefixes = new ArrayList<>();

  private int maxPayloadLength = 1000;

  private boolean logReqBody = true;

  private boolean logTraceId = true;

  private final WebLoggerFormatter formatter;

  public RequestLoggerFilter(WebLoggerFormatter formatter) {
    this.formatter = formatter;
  }

  /**
   * Log each request and respponse with full Request URI, content payload and duration of the
   * request in ms.
   *
   * @param request the request
   * @param response the response
   * @param filterChain chain of filters
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String url = request.getServletPath();
    for (String prefix : ignorePrefixes) {
      if (url.startsWith(prefix)) {
        filterChain.doFilter(request, response);
        return;
      }
    }

//    //从请求头中取出X-Request-Id，用于全局跟踪ID，如果未找到，自动生成一个新的跟踪ID
    String traceId = RequestCorrelationHolder.get();
    if (Strings.isNullOrEmpty(traceId)) {
      traceId = UUID.randomUUID().toString();
    }
    MDC.put("x-request-id", traceId);

    // ========= Log request and response payload ("body") ========
    // We CANNOT simply read the request payload here, because then the InputStream would be
    // consumed and cannot be read again by the actual processing/server.
    //    String reqBody = DoogiesUtil._stream2String(request.getInputStream());   // THIS WOULD
    // NOT WORK!
    // So we need to apply some stronger magic here :-)
    ResettableStreamRequestWrapper wrappedRequest
        = new ResettableStreamRequestWrapper(request);
    RequestLogContext requestLogContext = LogUtils.createRequestContext(wrappedRequest);
    requestLogContext.setId(traceId);
    ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

    // I can only log the request's body AFTER the request has been made and
    // ContentCachingRequestWrapper did its work.

    logReceviced(traceId, wrappedRequest);
    long startTime = System.currentTimeMillis();
    filterChain.doFilter(wrappedRequest,
        wrappedResponse);     // ======== This performs the actual request!
    wrappedResponse.addHeader("X-Request-Id", traceId);
    logSend(traceId, startTime, wrappedResponse);

    wrappedResponse
        .copyBodyToResponse();
    MDC.remove("x-request-id");
    // IMPORTANT: copy content of response back into original response
  }

  public void setLogReqBody(boolean logReqBody) {
    this.logReqBody = logReqBody;
  }

  public void setLogTraceId(boolean logTraceId) {
    this.logTraceId = logTraceId;
  }

  private void logSend(String traceId, long startTime, ContentCachingResponseWrapper response) {
    if (logTraceId) {
      // 跟踪ID:::HTTP:::SS:::响应码:::耗时:::响应字节数:::响应头
      LOGGER.info("{}:::HTTP:::SS:::{}:::{}ms:::{}:::{}", traceId, response.getStatus(),
          System.currentTimeMillis() - startTime,
          response.getContentAsByteArray().length,
          respHeaderString(response));
    } else {
      LOGGER.info("HTTP:::SS:::{}:::{}ms:::{}:::{}", response.getStatus(),
          System.currentTimeMillis() - startTime,
          response.getContentAsByteArray().length,
          respHeaderString(response));
    }
  }

  private void logReceviced(String traceId, ResettableStreamRequestWrapper request) {
    String body = request.getRequestBody();
    if (logTraceId) {
      // 跟踪ID:::HTTP:::SR:::调用方IP:::请求方法 请求地址:::请求体字节数:::请求头:::请求参数:::请求体
      LOGGER
          .info("{}:::HTTP:::SR:::{}:::{} {}:::{}bytes:::{}:::{}:::{}", traceId,
              getClientIp(request),
              request.getMethod(), request.getServletPath(),
              Strings.isNullOrEmpty(body) ? "0" : body.getBytes().length,
              headerString(request), paramString(request),
              !logReqBody || Strings.isNullOrEmpty(body) ? "-" : body);
    } else {
      LOGGER
          .info("HTTP:::SR:::{}:::{} {}:::{}bytes:::{}:::{}:::{}", getClientIp(request),
              request.getMethod(), request.getServletPath(),
              !logReqBody || Strings.isNullOrEmpty(body) ? "0" : body.getBytes().length,
              headerString(request), paramString(request),
              !logReqBody || Strings.isNullOrEmpty(body) ? "-" : body);
    }
  }

  public void addIgnorePrefix(String prefix) {
    ignorePrefixes.add(prefix);
  }

  @Deprecated
  private String getContentAsString(byte[] buf, int maxLength, String charsetName) {
    if (buf == null || buf.length == 0) {
      return "";
    }
    int length = Math.min(buf.length, this.maxPayloadLength);
    try {
      return new String(buf, 0, length, charsetName);
    } catch (UnsupportedEncodingException ex) {
      return "Unsupported Encoding";
    }
  }

  private Map<String, Object> respHeaderString(HttpServletResponse response) {
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

  private String getClientIp(HttpServletRequest request) {
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
}
