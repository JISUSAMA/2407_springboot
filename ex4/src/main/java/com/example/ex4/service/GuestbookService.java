//src/main/java/com/example/ex4/service/GuestbookService.java
package com.example.ex4.service;

import com.example.ex4.dto.GuestbookDTO;
import com.example.ex4.dto.PageRequestDTO;
import com.example.ex4.dto.PageResultDTO;
import com.example.ex4.entity.Guestbook;

public interface GuestbookService {
  Long register(GuestbookDTO dto);

  PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO pageRequestDTO);

  default Guestbook dtoToEntity(GuestbookDTO dto) {
    Guestbook guestbook = Guestbook.builder()
        .gno(dto.getGno())
        .title(dto.getTitle())
        .content(dto.getContent())
        .writer(dto.getWriter())
        .build();
    return guestbook;
  }
  default GuestbookDTO entityToDto(Guestbook guestbook) {
    GuestbookDTO guestbookDTO = GuestbookDTO.builder()
        .gno(guestbook.getGno())
        .title(guestbook.getTitle())
        .content(guestbook.getContent())
        .writer(guestbook.getWriter())
        .regDate(guestbook.getRegDate())
        .modDate(guestbook.getModDate())
        .build();
    return guestbookDTO;
  }
}
