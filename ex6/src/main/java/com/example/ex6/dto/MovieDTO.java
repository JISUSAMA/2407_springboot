package com.example.ex6.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {
  private Long mno;
  private String title;

  @Builder.Default //속성의 기본값으로 초기화
  private List<MovieImageDTO> imageDTOList = new ArrayList<>();

  private double avg;
  private int reviewCnt;

  private LocalDateTime regDate;
  private LocalDateTime modDate;
}
