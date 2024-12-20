package com.example.ex5.controller;

import com.example.ex5.dto.ReplyDTO;
import com.example.ex5.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {
  private final ReplyService replyService;

  @GetMapping(value = "/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ReplyDTO>> getList(@PathVariable("bno") Long bno) {
    log.info("bno: " + bno);

    return new ResponseEntity<>(replyService.getList(bno), HttpStatus.OK);
  }
  @PostMapping({"","/"})
  public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO) {
    log.info(">>"+replyDTO);
    Long rno = replyService.register(replyDTO);
    return new ResponseEntity<>(rno, HttpStatus.OK);
  }

  @PutMapping({"","/"})
  public ResponseEntity<Long> modify(@RequestBody ReplyDTO replyDTO) {
    log.info(">>"+replyDTO);
    replyService.modify(replyDTO);
    return new ResponseEntity<>(replyDTO.getRno(), HttpStatus.OK);
  }
  @DeleteMapping("/{rno}")
  public ResponseEntity<Long> delete(@PathVariable("rno") Long rno) {
    log.info(">>"+rno);
    replyService.remove(rno);
    return new ResponseEntity<>(rno, HttpStatus.OK);
  }




}
