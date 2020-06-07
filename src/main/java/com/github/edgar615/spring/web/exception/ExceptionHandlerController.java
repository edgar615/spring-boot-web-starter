package com.github.edgar615.spring.web.exception;

import com.github.edgar615.spring.web.WebProperties;
import com.github.edgar615.util.exception.DefaultErrorCode;
import com.github.edgar615.util.exception.ErrorCode;
import com.github.edgar615.util.exception.SystemException;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring MVC的异常处理类.
 *
 * @author Edgar
 * @version 1.0
 */
@ControllerAdvice
public class ExceptionHandlerController {

  @Autowired
  private ErrorStatusProperties errorStatusProperties;

  private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerController.class);

  @ExceptionHandler(value = NoHandlerFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ModelAndView defaultErrorHandler(NoHandlerFoundException ex,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
    SystemException systemException = SystemException.create(DefaultErrorCode.RESOURCE_NOT_FOUND)
            .set("method", ex.getHttpMethod())
            .set("url", ex.getRequestURL());
    return handleSystemException(systemException, request, response);
  }

  @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ModelAndView methodNotSupportedException(HttpRequestMethodNotSupportedException ex,
      HttpServletRequest request,
      HttpServletResponse response) {
    SystemException systemException = SystemException.create(DefaultErrorCode.INVALID_REQ)
        .set("method", ex.getMethod())
        .set("supportedMethods", ex.getSupportedHttpMethods());
    return handleSystemException(systemException, request, response);
  }

  /**
   * HttpServerErrorException.
   * http状态码为503
   *
   * @param ex HttpServerErrorException
   * @return ModelAndView
   */
  @ExceptionHandler(HttpServerErrorException.class)
  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  public ModelAndView handleHttpServerException(HttpServerErrorException ex,
                                                HttpServletRequest request,
                                                HttpServletResponse response) {
    ErrorCode errorCode = createError(999, ex.getMessage());
    SystemException systemException = SystemException.wrap(errorCode, ex)
            .set("details", request.getServletPath());
    return handleSystemException(systemException, request, response);
  }

  /**
   * Exception.
   * http状态码为400
   *
   * @param ex Exception
   * @return ModelAndView
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ModelAndView handleException(Exception ex, HttpServletRequest request,
                                      HttpServletResponse response) {
    ErrorCode errorCode = createError(999, ex.getMessage());
    SystemException systemException = SystemException.wrap(errorCode, ex)
            .set("details", request.getServletPath());
    return handleSystemException(systemException, request, response);
  }

  /**
   * BindException.
   * http状态码为400
   *
   * @param ex BindException
   * @return ModelAndView
   */
  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ModelAndView handleBindException(BindException ex, HttpServletRequest request,
                                          HttpServletResponse response) {
    BindingResult result = ex.getBindingResult();
    Multimap<String, String> errorDetail = ArrayListMultimap.create();
    if (result.hasErrors()) {
      List<FieldError> errors = result.getFieldErrors();
      for (FieldError error : errors) {
        errorDetail.put(error.getField(), error.getCode() + ":" + error.getDefaultMessage());
      }
    }
    SystemException systemException = SystemException.create(DefaultErrorCode.INVALID_ARGS)
            .set("details", errorDetail);
    return handleSystemException(systemException, request, response);
  }


  /**
   * HttpMessageNotReadableException.
   * http状态码为400
   *
   * @param ex HttpMessageNotReadableException
   * @return ModelAndView
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ModelAndView handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
                                                            HttpServletRequest request,
                                                            HttpServletResponse response) {
    SystemException systemException = SystemException.create(DefaultErrorCode.INVALID_REQ)
            .set("details", ex.getMessage());
    return handleSystemException(systemException, request, response);
  }

  /**
   * MissingServletRequestParameterException.
   * http状态码为400
   *
   * @param ex HttpMessageNotReadableException
   * @return ModelAndView
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ModelAndView handleMissingServletRequestParameterException(
          MissingServletRequestParameterException ex, HttpServletRequest request,
          HttpServletResponse response) {
    SystemException systemException = SystemException.create(DefaultErrorCode.MISSING_ARGS)
            .set("parameter", ex.getParameterName());
    return handleSystemException(systemException, request, response);
  }

  /**
   * IllegalArgumentException.
   * http状态码为400
   *
   * @param ex IllegalArgumentException
   * @return ModelAndView
   */
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ModelAndView handleIllegalArgumentException(
          IllegalArgumentException ex, HttpServletRequest request,
          HttpServletResponse response) {
    SystemException systemException = SystemException.create(DefaultErrorCode.INVALID_ARGS)
            .set("details", ex.getMessage());
    return handleSystemException(systemException, request, response);
  }

  /**
   * TypeMismatchException.
   * http状态码为400
   *
   * @param ex TypeMismatchException
   * @return ModelAndView
   */
  @ExceptionHandler(TypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ModelAndView handleTypeMismatchException(
          TypeMismatchException ex, HttpServletRequest request,
          HttpServletResponse response) {
    SystemException systemException = SystemException.create(DefaultErrorCode.INVALID_TYPE)
            .set("expected", ex.getRequiredType());
    return handleSystemException(systemException, request, response);
  }

  /**
   * MethodArgumentNotValidException.
   * http状态码为400
   *
   * @param ex MethodArgumentNotValidException
   * @return ModelAndView
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ModelAndView handleMethodArgumentNotValidException(
          MethodArgumentNotValidException ex,
          HttpServletRequest request,
          HttpServletResponse response) {
    BindingResult result = ex.getBindingResult();
    Multimap<String, String> errorDetail = ArrayListMultimap.create();
    if (result.hasErrors()) {
      List<FieldError> errors = result.getFieldErrors();
      for (FieldError error : errors) {
        errorDetail.put(error.getField(), error.getCode() + ":" + error.getDefaultMessage());
      }
    }
    SystemException systemException = SystemException.create(DefaultErrorCode.INVALID_ARGS)
            .set("details", errorDetail);
    return handleSystemException(systemException, request, response);
  }

  /**
   * SystemException.
   * http状态码默认为400，如果想修改，通过配置修改,
   *
   * @param ex       SystemException
   * @param request  HttpServletRequest
   * @param response HttpServletResponse
   * @return ModelAndView
   */
  @ExceptionHandler(SystemException.class)
  public ModelAndView handleSystemException(
          SystemException ex, HttpServletRequest request, HttpServletResponse response) {
    ErrorCode errorCode = ex.getErrorCode();
    if (errorCode.getNumber() == DefaultErrorCode.UNKOWN.getNumber()) {
      LOGGER.warn("ex:::{}::{}", ex.getErrorCode().getNumber(), ex.getErrorCode().getMessage(), ex);
//    } else if (webProperties.getLogConfig() != null && webProperties.getLogConfig().isShowErrorStackTrace()) {
//      LOGGER.warn("ex:::{}::{}", ex.getErrorCode().getNumber(), ex.getErrorCode().getMessage(), ex);
    } else {
      LOGGER.warn("ex:::{}::{}", ex.getErrorCode().getNumber(), ex.getErrorCode().getMessage());
    }

    if (errorStatusProperties.getError() != null) {
      Map<Integer, Integer> error = errorStatusProperties.getError();
      Integer statusCode = error.getOrDefault(errorCode.getNumber(), 400);
      response.setStatus(statusCode);
    } else {
      response.setStatus(400);
    }
    String contentType = request.getContentType();
    if (contentType != null && contentType.startsWith("application/json")) {
      ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
      mav.addObject("code", ex.getErrorCode().getNumber());
      mav.addObject("message", ex.getErrorCode().getMessage());
      mav.addAllObjects(ex.getProperties());
      return mav;
    } else {
      ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
      mav.addObject("code", ex.getErrorCode().getNumber());
      mav.addObject("message", ex.getErrorCode().getMessage());
      mav.addAllObjects(ex.getProperties());
      return mav;
    }

  }

//  /**
//   * SystemException.
//   * http状态码为errorCode定义的状态码
//   *
//   * @param ex       SystemException
//   * @param request  HttpServletRequest
//   * @param response HttpServletResponse
//   * @return ModelAndView
//   */
//  @ExceptionHandler(ValidationException.class)
//  public ModelAndView handleValidationException(
//          ValidationException ex, HttpServletRequest request, HttpServletResponse response) {
//    SystemException systemException = SystemException.create(DefaultErrorCode.INVALID_ARGS);
//    if (!ex.getErrorDetail().isEmpty()) {
//      systemException.set("details", ex.getErrorDetail().asMap());
//    }
//    return handleSystemException(systemException, request, response);
//  }

  private ErrorCode createError(int code, String message) {
    return new ErrorCode() {
      @Override
      public int getNumber() {
        return code;
      }

      @Override
      public String getMessage() {
        return message;
      }

    };
  }

}
