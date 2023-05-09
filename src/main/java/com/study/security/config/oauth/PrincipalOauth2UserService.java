package com.study.security.config.oauth;


import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

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
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest = " + userRequest);
        String tokenValue = userRequest.getAccessToken().getTokenValue();
        System.out.println("tokenValue = " + tokenValue);
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        System.out.println("clientRegistration = " + clientRegistration);
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("super.loadUser(userRequest).getAttributes() = " + oAuth2User.getAttributes());
        return oAuth2User;
    }
}
