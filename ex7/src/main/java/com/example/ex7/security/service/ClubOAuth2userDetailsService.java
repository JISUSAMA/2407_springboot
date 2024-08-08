package com.example.ex7.security.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Log4j2
@Service
public class ClubOAuth2userDetailsService extends DefaultOAuth2UserService {
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    log.info("================ userRequest: "+userRequest);
//    String clientName = userRequest.getClientRegistration().getClientName();
//    log.info("ClientName >> ",clientName);
//    log.info("info_from_google >> ", userRequest.getAdditionalParameters());
//    OAuth2User oAuth2User = super.loadUser(userRequest);
//    oAuth2User.getAttributes().forEach((k,v) ->{log.info(k+":"+v);});

    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);
    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    log.info("registrationId >> ",registrationId);
    SocialType socialType = getSocialType(registrationId);
    log.info("socialType >> ",socialType);
    String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
        .getUserInfoEndpoint().getUserNameAttributeName();
    Map<String, Object> attributes = oAuth2User.getAttributes();
    for(Map.Entry<String, Object> entry : attributes.entrySet()){
      System.out.println(entry.getKey()+":"+entry.getValue());
    }
    return super.loadUser(userRequest);  //서비스에서 가져온 유저 정보를 담음
  }
  private SocialType getSocialType(String registrationId){
    if(SocialType.NAVER.name().equals(registrationId)){
      log.info(">>NAVER>>");
      return SocialType.NAVER;
    }
    if(SocialType.KAKAO.name().equals(registrationId)){
      log.info(">>KAKAO>>");
      return SocialType.KAKAO;
    }
    log.info(">>GOOGLE >>",SocialType.GOOGLE.name());
    return SocialType.GOOGLE;
  }
  private  enum SocialType{
    KAKAO,NAVER,GOOGLE
  }
}
