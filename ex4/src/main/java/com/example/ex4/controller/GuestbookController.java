//src/main/java/com/example/ex4/controller/GuestbookController.java
package com.example.ex4.controller;
import com.example.ex4.service.GuestbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor
public class GuestbookController {
  //GuestbookService 인터페이스 선언
  private final GuestbookService guestbookService;

  @GetMapping({"","/","/list"})
  public String list(){
    return "/guestbook/list";
  }
}
