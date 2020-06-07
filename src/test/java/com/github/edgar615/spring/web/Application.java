package com.github.edgar615.spring.web;

import com.github.edgar615.spring.web.response.ResponseResult;
import com.github.edgar615.util.exception.CustomErrorCode;
import com.github.edgar615.util.exception.SystemException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@SpringBootApplication(scanBasePackages = {"com.github.edgar615.**"})
@RestController
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @RequestMapping(value = "/test", method = RequestMethod.GET)
  @ResponseResult
  public User test(@RequestParam("id") Integer id) {
    throw SystemException.create(CustomErrorCode.create(1000, "test"));
//    User user = new User();
//    user.setId(UUID.randomUUID().toString());
//    return user;
  }

}
