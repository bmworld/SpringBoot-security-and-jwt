package com.study.security.config.oauth;


import com.study.security.config.SecurityConfig;
import com.study.security.config.auth.PrincipalDetails;
import com.study.security.config.oauth.provider.FaceBookUserInfo;
import com.study.security.config.oauth.provider.GoogleUserInfo;
import com.study.security.config.oauth.provider.NaverUserInfo;
import com.study.security.config.oauth.provider.OAuth2UserInfo;
import com.study.security.domain.Member;
import com.study.security.domain.RoleType;
import com.study.security.respotiroy.MemberRepository;
import com.study.security.util.RandomUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
  
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final MemberRepository memberRepository;
  
  
  /**
   * <h2>loadUser: 구글에서 전달받은 Data 후처리 함수</h2>
   *
   * <pre>
   *  - username : google_{구글 고유값(sub)}
   *  - password : "암호화(임의 생성)"
   *     => 사용자는 어차피 username, password로 로그인하지 않음
   *  - email : {google Email}
   *  - role: "ROLE_USER"
   *  - provider: "google"
   *  - providerId: {구글 고유값(sub)}
   * </pre>
   *
   * @super.loadUser(userRequest).getAttributes() 구글에서 전달받은 사용자 데이터
   */
  @Override
  @Transactional
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    // PROCESS: 구글 로그인 버튼 > 구글 로그인페이지 Redir > 로긴 완료 > OAuth2-Client 라이브러리 > Access Token 요청 > userRequest 정보 > loadUser 메서드 호출 > 구글 프로필정보 이용가능
    String tokenValue = userRequest.getAccessToken().getTokenValue();
    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    ClientRegistration clientRegistration = userRequest.getClientRegistration();
    OAuth2User oAuth2User = super.loadUser(userRequest);
    Map<String, Object> attributes = oAuth2User.getAttributes();
    System.out.println("----- PrincipalOauth2UserService > super.loadUser(userRequest).getAttributes() = " + attributes);
    
    OAuth2UserInfo oauth2UserInfo = null;
    
    if (registrationId.equals("google")) {
      oauth2UserInfo = new GoogleUserInfo(attributes);
    } else if (registrationId.equals("naver")) {
      oauth2UserInfo = new NaverUserInfo((Map) attributes.get("response"));
    } else {
      System.out.println("----- 현재 앱은 구글, 네이버만 지원합니다. (페이스북 미지원) ");
    }

    String provider = oauth2UserInfo.getProvider();
    String providerId = oauth2UserInfo.getProviderId();
    String username = provider + "_" + providerId;
    String password = bCryptPasswordEncoder.encode(RandomUtils.generateSecureRandomString(10));
    String email = oauth2UserInfo.getEmail();
    RoleType roleType = RoleType.USER;
    
    Optional<Member> optionalMember = memberRepository.findByUsername(username);
    // 기존 회원이 아닐 경우
    Member member = optionalMember.isPresent() ? optionalMember.get() : null;
    if (member == null) {
      member = Member.builder()
        .username(username)
        .password(password)
        .email(email)
        .role(roleType)
        .loginDate(now())
        .provider(provider)
        .providerId(providerId)
        .build();
      memberRepository.save(member);
    } else {
      member.setLoginDate(now());
    }
    
    return new PrincipalDetails(member, attributes);
  }
}
