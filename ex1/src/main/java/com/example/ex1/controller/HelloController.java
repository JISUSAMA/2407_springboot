package com.example.ex1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//SSR(Server Side Rendering) 방식 : 웹페이지를 주로 전송하는 방식
@Controller
@RequestMapping("/")
public class HelloController
{
  //view를 위한 Template Engine이 추가 되어있지 않는 경우는 Static 활용
  // 라이브러리가 추가 되어 있지 않은 경우는 static 활용
  @GetMapping("/hello")
  public String hello(){
    return "hello.html";
  }
}
