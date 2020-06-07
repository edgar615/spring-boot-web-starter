package com.github.edgar615.spring.web.log;

import java.util.List;

public class WebLogConfig {

  /**
   *  是否打印请求体
   */
  private boolean showReqBody = false;

  /**
   * 输出错误的堆栈信息，默认目前只有未知错误才打印
   */
  private boolean showErrorStackTrace = true;

  /**
   * 忽略日志的路径
   */
  private List<String> ignoreLogPath;

  /**
   * 参数黑名单，包括请求头，请求参数，请求体，响应头
   */
  private List<String> paramBlacklist;

  /**
   * 黑名单替换的字符串
   */
  private String scrubbedValue = "xxxxx";

  public List<String> getIgnoreLogPath() {
    return ignoreLogPath;
  }

  public void setIgnoreLogPath(List<String> ignoreLogPath) {
    this.ignoreLogPath = ignoreLogPath;
  }

  public boolean isShowReqBody() {
    return showReqBody;
  }

  public void setShowReqBody(boolean showReqBody) {
    this.showReqBody = showReqBody;
  }

  public boolean isShowErrorStackTrace() {
    return showErrorStackTrace;
  }

  public void setShowErrorStackTrace(boolean showErrorStackTrace) {
    this.showErrorStackTrace = showErrorStackTrace;
  }
}
