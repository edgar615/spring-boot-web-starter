package com.github.edgar615.spring.web.response;

import com.github.edgar615.spring.web.WebConsts;
import com.google.common.base.Strings;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@ControllerAdvice
public class ResponseResultAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String hasResponseResultAnnotation = (String) attributes.getAttribute(WebConsts.RESPONSE_RESULT_ANNOTATION, SCOPE_REQUEST);
        return !Strings.isNullOrEmpty(hasResponseResultAnnotation);
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        // String直接报错，还不知道原因
        if (o instanceof String) {
            return o;
        }
        if (o instanceof RestResult) {
            return o;
        }
        if (o instanceof ModelAndView) {
            return o;
        }
        return RestResult.success(o);
    }
}
