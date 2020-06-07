package com.github.edgar615.spring.web.correlation;

@Deprecated
public enum RequestCorrelationType {
  /**
   * uuid生成
   */
  UUID,

  /**
   * 从请求头获取
   */
  HEADER
}
