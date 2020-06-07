package com.github.edgar615.spring.web.response;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.HashMap;
import java.util.Map;

/**
 * rest返回的视图.
 *
 * @author Edgar
 * @version 1.0
 */
public final class ResponseMessage {

  private ResponseMessage() {
    super();
  }

  public static ModelAndView asSuccess() {
    return asModelAndView(1);
  }

  /**
   * 根据参数返回视图.
   * 返回的视图包括一个个属性 <code>result :  {result}</code>
   *
   * @param result 结果
   * @return 视图
   */
  public static ModelAndView asModelAndView(Object result) {
    MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
    jsonView.setExtractValueFromSingleKeyModel(false);
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("result", result);
    return new ModelAndView(jsonView, map);
  }

  /**
   * 根据参数返回视图.
   * 返回的视图包括两个属性 <code>code : {code}, message :  {mesage}</code>
   *
   * @param code    编码
   * @param message 对象
   * @return 视图
   */
  public static ModelAndView asModelAndView(int code, Object message) {
    MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
    jsonView.setExtractValueFromSingleKeyModel(true);
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("message", message);
    map.put("code", code);
    return new ModelAndView(jsonView, map);
  }

}
