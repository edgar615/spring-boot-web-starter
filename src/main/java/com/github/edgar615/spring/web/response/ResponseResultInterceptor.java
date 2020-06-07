package com.github.edgar615.spring.web.response;

import com.github.edgar615.spring.web.WebConsts;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class ResponseResultInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Class clazz = handlerMethod.getBeanType();
            Method method = handlerMethod.getMethod();
            if (clazz.isAnnotationPresent(ResponseResult.class)) {
                request.setAttribute(WebConsts.RESPONSE_RESULT_ANNOTATION, WebConsts.RESPONSE_RESULT_ANNOTATION);
            }
            if (method.isAnnotationPresent(ResponseResult.class)) {
                request.setAttribute(WebConsts.RESPONSE_RESULT_ANNOTATION, WebConsts.RESPONSE_RESULT_ANNOTATION);
            }
        }

        return true;
    }
}
