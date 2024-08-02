package com.example.ex6.service;

import com.example.ex6.dto.ReviewDTO;
import com.example.ex6.entity.Member;
import com.example.ex6.entity.Movie;
import com.example.ex6.entity.Review;

import java.util.List;

public interface ReviewService {
  List<ReviewDTO> getListofMoive(Long mno);
  Long register(ReviewDTO reviewDTO);
  void modify(ReviewDTO reviewDTO);
  void remove(Long reviewnum);
  public default Review dtoToEntity(ReviewDTO reviewDTO) {
    Review review = Review.builder()
        .reviewnum(reviewDTO.getReviewnum())
        .movie(Movie.builder().mno(reviewDTO.getMno()).build())
        .member(Member.builder().mid(reviewDTO.getMid()).build())
        .grade(reviewDTO.getGrade())
        .text(reviewDTO.getText())
        .build();
    return review;
  }

  public default ReviewDTO entityToDto(Review review) {
    ReviewDTO reviewDTO = ReviewDTO.builder()
        .reviewnum(review.getReviewnum())
        .text(review.getText())
        .mno(review.getMovie().getMno())
        .mid(review.getMember().getMid())
        .nickname(review.getMember().getNickname())
        .email(review.getMember().getEmail())
        .grade(review.getGrade())
        .text(review.getText())
        .regDate(review.getRegDate())
        .modDate(review.getModDate())
        .build();
    return reviewDTO;
  }
}