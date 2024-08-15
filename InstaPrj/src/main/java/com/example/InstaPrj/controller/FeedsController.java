package com.example.InstaPrj.controller;

import com.example.InstaPrj.dto.FeedsDTO;
import com.example.InstaPrj.dto.PageRequestDTO;
import com.example.InstaPrj.service.FeedsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.net.URLDecoder;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/movie")
@RequiredArgsConstructor
public class FeedsController {
  private final FeedsService feedsService;

  @GetMapping("/register")
  public void register() {
  }

  @PostMapping("/register")
  public String registerPost(FeedsDTO feedsDTO, RedirectAttributes ra) {
    Long mno = feedsService.register(feedsDTO);
    ra.addFlashAttribute("msg", mno);
    return "redirect:/movie/list";
  }

  @GetMapping({"","/","/list"})
  public String list(PageRequestDTO pageRequestDTO, Model model) {
    model.addAttribute("pageResultDTO", feedsService.getList(pageRequestDTO));
    return "/movie/list";
  }

  @GetMapping({"/read", "/modify"})
  public void getMovie(Long fno, PageRequestDTO pageRequestDTO, Model model) {
    FeedsDTO feedsDTO = feedsService.getFeeds(fno);
    typeKeywordInit(pageRequestDTO);
    model.addAttribute("feedsDTO", feedsDTO);
  }
  @PostMapping("/modify")
  public String modify(FeedsDTO dto, RedirectAttributes ra, PageRequestDTO pageRequestDTO){
    log.info("modify post... dto: " + dto);
    movieService.modify(dto);
    typeKeywordInit(pageRequestDTO);
    ra.addFlashAttribute("msg", dto.getMno() + " 수정");
    ra.addAttribute("mno", dto.getMno());
    ra.addAttribute("page", pageRequestDTO.getPage());
    ra.addAttribute("type", pageRequestDTO.getType());
    ra.addAttribute("keyword", pageRequestDTO.getKeyword());
    return "redirect:/movie/read";
  }

  @Value("${com.example.upload.path}")
  private String uploadPath;

  @PostMapping("/remove")
  public String remove(Long mno, RedirectAttributes ra, PageRequestDTO pageRequestDTO){
    log.info("remove post... mno: " + mno);
    List<String> result = feedsService.removeWithReviewsAndMovieImages(mno);
    log.info("result>>"+result);
    result.forEach(fileName -> {
      try {
        log.info("removeFile............"+fileName);
        String srcFileName = URLDecoder.decode(fileName, "UTF-8");
        File file = new File(uploadPath + File.separator + srcFileName);
        file.delete();
        File thumb = new File(file.getParent(),"s_"+file.getName());
        thumb.delete();
      } catch (Exception e) {
        log.info("remove file : "+e.getMessage());
      }
    });
    if(movieService.getList(pageRequestDTO).getDtoList().size() == 0 && pageRequestDTO.getPage() != 1) {
      pageRequestDTO.setPage(pageRequestDTO.getPage()-1);
    }
    typeKeywordInit(pageRequestDTO);
    ra.addFlashAttribute("msg", mno + " 삭제");
    ra.addAttribute("page", pageRequestDTO.getPage());
    ra.addAttribute("type", pageRequestDTO.getType());
    ra.addAttribute("keyword", pageRequestDTO.getKeyword());
    return "redirect:/movie/list";
  }
  private void typeKeywordInit(PageRequestDTO pageRequestDTO){
    if (pageRequestDTO.getType().equals("null")) pageRequestDTO.setType("");
    if (pageRequestDTO.getKeyword().equals("null")) pageRequestDTO.setKeyword("");
  }
}