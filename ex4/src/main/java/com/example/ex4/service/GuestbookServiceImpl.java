//src/main/java/com/example/ex4/service/GuestbookServiceImpl.java
package com.example.ex4.service;

import com.example.ex4.dto.GuestbookDTO;
import com.example.ex4.dto.PageRequestDTO;
import com.example.ex4.dto.PageResultDTO;
import com.example.ex4.entity.Guestbook;
import com.example.ex4.repository.GuestbookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class GuestbookServiceImpl implements GuestbookService {

  private final GuestbookRepository guestbookRepository;

  @Override
  public Long register(GuestbookDTO dto) {
    Guestbook guestbook = dtoToEntity(dto);
    guestbookRepository.save(guestbook);
    return guestbook.getGno();
  }

  @Override
  public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO pageRequestDTO) {
    // 알고자하는 페이지(번호, 갯수, 정렬)
    Pageable pageable = pageRequestDTO.getPageable(Sort.by("gno").descending());

    //Page<Guestbook> 원하는 페이지의 목록(복수)
    //repository에서 복수개의 데이터를 받을 때 페이징 처리를 할려면 Page<>를 활용
    //repository에서 복수개의 데이터를 받을 때 페이징 불필요하면 List<>활용
    Page<Guestbook> result = guestbookRepository.findAll(pageable);
    // 목록을 처리하기 위한 함수 정의
    //Page의 Guestbook 을 GuestbookDTO 로 변환해주는 함수
    Function<Guestbook, GuestbookDTO> fn = new Function<Guestbook, GuestbookDTO>() {
      @Override
      public GuestbookDTO apply(Guestbook guestbook) {
        return entityToDto(guestbook);
      }
    };
    // result는 요청페이지의 목록,
    // fn은 result의 원소(Guestbook)를 GuestbookDTO로 변환기능
    return new PageResultDTO<>(result, fn);
  }

  @Override
  public GuestbookDTO read(Long gno) {
    //단수 : findById를 통해서 유일한 하나의 객체를 찾아보는 것
    //Optional의 특징 : null 값을 받아도 에러가 발생하지않고 형변환 안해도 안전하다

    Optional<Guestbook> result = guestbookRepository.findById(gno);
/*  GuestbookDTO guestbookDTO = null;
    if (result.isPresent()) {
      guestbookDTO = entityToDto(result.get()) ;
    }
    return guestbookDTO;*/
    return result.isPresent() ? entityToDto(result.get()) : null;
  }

  @Override
  public void modify(GuestbookDTO guestbookDTO) {
    Optional<Guestbook> result
        = guestbookRepository.findById(guestbookDTO.getGno());
    if (result.isPresent()) {
      Guestbook guestbook = result.get();
      guestbook.changeTitle(guestbookDTO.getTitle());
      guestbook.changeContent(guestbookDTO.getContent());
      guestbookRepository.save(guestbook);
    }
  }
  @Override
  public void remove(GuestbookDTO guestbookDTO) {
    Optional<Guestbook> result = guestbookRepository.findById(guestbookDTO.getGno());
    if (result.isPresent()) {
      guestbookRepository.deleteById(guestbookDTO.getGno());
    }
  }
}
