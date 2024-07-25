package com.example.ex5.controller;

import com.example.ex5.dto.ReplyDTO;
import com.example.ex5.entity.Board;
import com.example.ex5.service.ReplyService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //repregentation 대의적인 상태를 전송하는 것
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {
  private final ReplyService replyService;

  @GetMapping(value = "/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ReplyDTO>> getList(@PathVariable("bno") Long bno ) {
    log.info("bno: "+bno);
    return new ResponseEntity<>(replyService.getList(bno), HttpStatus.OK);

  }

  @PostMapping({"","/"})
  public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO ) {
    log.info(replyDTO);
    Long rno = replyService.register(replyDTO);
    return new ResponseEntity<>(rno, HttpStatus.OK);

  }
}
