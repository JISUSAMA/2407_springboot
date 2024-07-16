//src/main/java/com/example/ex4/controller/GuestbookController.java
package com.example.ex4.controller;

import com.example.ex4.dto.GuestbookDTO;
import com.example.ex4.dto.PageRequestDTO;
import com.example.ex4.dto.PageResultDTO;
import com.example.ex4.entity.Guestbook;
import com.example.ex4.service.GuestbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor
public class GuestbookController {

  private final GuestbookService guestbookService;

  @GetMapping({"", "/", "/list"})
  public String list(Model model, PageRequestDTO pageRequestDTO) {
    // 페이지에 대한 요청 정보를 통해서 최종 PageResultDTO 생성
    model.addAttribute("pageResultDTO", guestbookService.getList(pageRequestDTO));
    return "/guestbook/list";
  }

  @GetMapping("/register")
  public void registerGet() {
    log.info("register get.....");
  }

  @PostMapping("/register")
  public String registerPost(GuestbookDTO guestbookDTO, RedirectAttributes ra) {
    log.info("register post........");
    Long gno = guestbookService.register(guestbookDTO);
    ra.addFlashAttribute("msg", gno+"번이 등록");
    // redirect는 컨트롤러로 재전송한다는 의미
    return "redirect:/guestbook/list";
  }

  @GetMapping({"/read", "/modify"})
  public void read(Long gno, int page, Model model) {
    GuestbookDTO guestbookDTO = guestbookService.read(gno);
    model.addAttribute("guestbookDTO", guestbookDTO);
    model.addAttribute("page", page);
  }

  @PostMapping("/modify")
  public String modify(GuestbookDTO guestbookDTO, PageRequestDTO pageRequestDTO
      , RedirectAttributes ra) {
    guestbookService.modify(guestbookDTO);
    ra.addFlashAttribute("msg",guestbookDTO.getGno()+"번이 수정");
    ra.addAttribute("page",pageRequestDTO.getPage());
    ra.addAttribute("gno",guestbookDTO.getGno());
    return "redirect:/guestbook/read";
  }
  @PostMapping("/remove")
  public String remove(GuestbookDTO guestbookDTO, PageRequestDTO pageRequestDTO
      , RedirectAttributes ra) {
    guestbookService.remove(guestbookDTO);

    if(guestbookService.getList(pageRequestDTO).getDtoList().size() ==0
        && pageRequestDTO.getPage() != 1){
      pageRequestDTO.setPage(pageRequestDTO.getPage()-1);
    }

    ra.addFlashAttribute("msg",guestbookDTO.getGno()+"번이 삭제");
    ra.addAttribute("page",pageRequestDTO.getPage());
    return "redirect:/guestbook/list";
  }
}