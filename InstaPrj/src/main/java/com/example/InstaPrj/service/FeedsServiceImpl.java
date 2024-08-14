package com.example.InstaPrj.service;

import com.example.InstaPrj.dto.FeedsDTO;
import com.example.InstaPrj.dto.PageRequestDTO;
import com.example.InstaPrj.dto.PageResultDTO;
import com.example.InstaPrj.entity.Feeds;
import com.example.InstaPrj.entity.Photos;
import com.example.InstaPrj.repository.FeedsRepository;
import com.example.InstaPrj.repository.PhotosRepository;
import com.example.InstaPrj.repository.ReviewsRepository;
import com.example.ex6.dto.MovieDTO;
import com.example.ex6.dto.PageRequestDTO;
import com.example.ex6.dto.PageResultDTO;
import com.example.ex6.entity.Movie;
import com.example.ex6.entity.MovieImage;
import com.example.ex6.repository.MovieImageRepository;
import com.example.ex6.repository.MovieRepository;
import com.example.ex6.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URLDecoder;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class FeedsServiceImpl implements FeedsService {
  private final FeedsRepository feedsRepository;
  private final PhotosRepository photosRepository;
  private final ReviewsRepository reviewsRepository;

  @Override
  public Long register(FeedsDTO feedsDTO) {
    Map<String, Object> entityMap = dtoToEntity(feedsDTO);
    Feeds feeds = (Feeds) entityMap.get("feeds");
    List<Photos> movieImageList =
        (List<Photos>) entityMap.get("photoList");
    feedsRepository.save(feeds);
    if(movieImageList != null) {
      movieImageList.forEach(new Consumer<Photos>() {
        @Override
        public void accept(Photos photos) {
          photosRepository.save(photos);
        }
      });
    }
    return feeds.getFno();
  }

  @Override
  public PageResultDTO<FeedsDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
    Pageable pageable = pageRequestDTO.getPageable(Sort.by("fno").descending());
    // Page<Movie> result = movieRepository.findAll(pageable);
//    Page<Object[]> result = movieRepository.getListPageImg(pageable);
    Page<Object[]> result = feedsRepository.searchPage(pageRequestDTO.getType(),
        pageRequestDTO.getKeyword(),
        pageable);
    Function<Object[], FeedsDTO> fn = objects -> entityToDto(
        (Feeds) objects[0],
        (List<Photos>) (Arrays.asList((Photos)objects[1])),
        (Double) objects[2],
        (Long) objects[3]
    );
    return new PageResultDTO<>(result, fn);
  }

  @Override
  public FeedsDTO getFeeds(Long fno) {
    List<Object[]> result = feedsRepository.getMovieWithAll(fno);
    Feeds feeds = (Feeds) result.get(0)[0];
    List<Photos> photos = new ArrayList<>();
    result.forEach(objects -> photos.add((Photos) objects[1]));
    Double avg = (Double) result.get(0)[2];
    Long reviewCnt = (Long) result.get(0)[3];

    return entityToDto(feeds, photos, avg, reviewCnt);
  }

  @Value("${com.example.upload.path}")
  private String uploadPath;

  @Transactional
  @Override
  public void modify(FeedsDTO feedsDTO) {
    Optional<Feeds> result = feedsRepository.findById(feedsDTO.getFno());
    if (result.isPresent()) {
      Map<String, Object> entityMap = dtoToEntity(feedsDTO);
      Feeds feeds = (Feeds) entityMap.get("feeds");
      feeds.changeTitle(feedsDTO.getTitle());
      feedsRepository.save(feeds);
      // movieImageList :: 수정창에서 이미지 수정할 게 있는 경우의 목록
      List<Photos> newMovieImageList =
          (List<Photos>) entityMap.get("movieImageList");
      List<Photos> oldMovieImageList =
          photosRepository.findByFno(feeds.getFno());
      if(newMovieImageList == null) {
        // 수정창에서 이미지 모두를 지웠을 때
        photosRepository.deleteByFno(feeds.getFno());
        for (int i = 0; i < oldMovieImageList.size(); i++) {
          Photos oldPhotos = oldMovieImageList.get(i);
          String fileName = oldPhotos.getPath() + File.separator
              + oldPhotos.getUuid() + "_" + oldPhotos.getImgName();
          deleteFile(fileName);
        }
      } else { // newMovieImageList에 일부 변화 발생
        newMovieImageList.forEach(movieImage -> {
          boolean result1 = false;
          for (int i = 0; i < oldMovieImageList.size(); i++) {
            result1 = oldMovieImageList.get(i).getUuid().equals(movieImage.getUuid());
            if(result1) break;
          }
          if(!result1) photosRepository.save(movieImage);
        });
        oldMovieImageList.forEach(oldMovieImage -> {
          boolean result1 = false;
          for (int i = 0; i < newMovieImageList.size(); i++) {
            result1 = newMovieImageList.get(i).getUuid().equals(oldMovieImage.getUuid());
            if(result1) break;
          }
          if(!result1) {
            movieImageRepository.deleteByUuid(oldMovieImage.getUuid());
            String fileName = oldMovieImage.getPath() + File.separator
                + oldMovieImage.getUuid() + "_" + oldMovieImage.getImgName();
            deleteFile(fileName);
          }
        });
      }
    }
  }

  private void deleteFile(String fileName) {
    // 실제 파일도 지우기
    String searchFilename = null;
    try {
      searchFilename = URLDecoder.decode(fileName, "UTF-8");
      File file = new File(uploadPath + File.separator + searchFilename);
      file.delete();
      new File(file.getParent(), "s_" + file.getName()).delete();
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  @Transactional
  @Override
  public List<String> removeWithReviewsAndMovieImages(Long mno) {
    List<MovieImage> list = movieImageRepository.findByMno(mno);
    List<String> result = new ArrayList<>();
    list.forEach(new Consumer<MovieImage>() {
      @Override
      public void accept(MovieImage t) {
        result.add(t.getPath() + File.separator + t.getUuid() + "_" + t.getImgName());
      }
    });
    movieImageRepository.deleteByMno(mno);
    reviewRepository.deleteByMno(mno);
    movieRepository.deleteById(mno);
    return result;
  }

  @Override
  public void removeUuid(String uuid) {
    log.info("deleteImage...... uuid: " + uuid);
    movieImageRepository.deleteByUuid(uuid);
  }
}
