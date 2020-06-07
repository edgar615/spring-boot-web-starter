package com.github.edgar615.spring.web.log;

import java.util.Map;

public class RequestLogContext implements LogContext {

  private final String CLIENT_IP_DIRECTIVE = "c";

  private final String USER_DIRECTIVE = "u";

  private final String RECEIVE_TIME_DIRECTIVE = "t";

  private final String METHOD_DIRECTIVE = "m";

  private final String URI_DIRECTIVE = "u";

  private final String QUERY_DIRECTIVE = "q";

  private final String HEADER_DIRECTIVE = "H";

  private final String BODY_DIRECTIVE = "B";

  private final String BODY_LEN_DIRECTIVE = "b";

  private String id;

  /**
   * 客户端IP
   */
  private String clientIp;

  /**
   * 用户,刚收到时读不到
   */
  @Deprecated
  private String user;

  /**
   * 请求时间
   */
  private Long receiveTime;

  /**
   * 请求方法
   */
  private String method;

  /**
   * 请求地址
   */
  private String uri;

  /**
   * 查询字符串
   */
  private Map<String, Object> query;

  private String body;

  private Map<String, Object> header;

  /**
   * 根据指令转换为值，这里没有做太复杂的模式，直接用if/else实现
   * @param percentDirective
   * @return
   */
  public Object get(String percentDirective) {
    if (CLIENT_IP_DIRECTIVE.equals(percentDirective)) {
      return getOrDefault(clientIp);
    }
    if (USER_DIRECTIVE.equals(percentDirective)) {
      return getOrDefault(user);
    }
    if (RECEIVE_TIME_DIRECTIVE.equals(percentDirective)) {
      return getOrDefault(receiveTime);
    }
    if (METHOD_DIRECTIVE.equals(percentDirective)) {
      return getOrDefault(method);
    }
    if (URI_DIRECTIVE.equals(percentDirective)) {
      return getOrDefault(uri);
    }
    if (QUERY_DIRECTIVE.equals(percentDirective)) {
      return getOrDefault(query);
    }
    if (HEADER_DIRECTIVE.equals(percentDirective)) {
      return getOrDefault(header);
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

  public String getClientIp() {
    return clientIp;
  }

  public void setClientIp(String clientIp) {
    this.clientIp = clientIp;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public Long getReceiveTime() {
    return receiveTime;
  }

  public void setReceiveTime(Long receiveTime) {
    this.receiveTime = receiveTime;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public Map<String, Object> getQuery() {
    return query;
  }

  public void setQuery(Map<String, Object> query) {
    this.query = query;
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
}
