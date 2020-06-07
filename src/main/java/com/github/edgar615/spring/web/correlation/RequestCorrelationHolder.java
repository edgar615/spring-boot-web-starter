package com.github.edgar615.spring.web.correlation;

public class RequestCorrelationHolder {

  private static final ThreadLocal<String> PRINCIPAL_HOLDER = new ThreadLocal<>();

  private RequestCorrelationHolder() {
    throw new AssertionError("Not instantiable: " + RequestCorrelationHolder.class);
  }

  public static void set(String principal) {
    PRINCIPAL_HOLDER.set(principal);
  }

  public static String get() {
    return PRINCIPAL_HOLDER.get();
  }

  public static void clear() {
    PRINCIPAL_HOLDER.remove();
  }

}
