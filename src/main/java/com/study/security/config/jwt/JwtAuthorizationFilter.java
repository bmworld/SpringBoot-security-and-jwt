package com.study.security.config.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.study.security.config.auth.PrincipalDetails;
import com.study.security.domain.Member;
import com.study.security.respotiroy.MemberRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * <pre>
 * Security 의 Filter 중, BasicAuthenticationFilter를 활용함.
 * 해당 권한 또는 인증이 필요한 특정한 주소 요청 시, 위 필터를 무조건 거침.
 * 만약 권한이나 인증이 필요한 주소가 아닐 경우, 해당 필터를 거치지 않음.
 * </pre>
 */

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

  private final MemberRepository memberRepository;


  public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository) {
    super(authenticationManager);
    this.memberRepository = memberRepository;
  }

  /**
   * <h1>SecurityConfig에 설정된 권한이 필요한 Path에 요청할 경우, doFilterInternal 작동</h1>
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    // ##################################################################
//    super.doFilterInternal(request, response, chain); // ! 사용 X => Why? chain.doFilter(request, response); 중복됨
    // ##################################################################
    System.out.println("----- JwtAuthorizationFilter = 인증이나 권한이 필요한 주소 요청");

    String jwtTokenWithPrefix = request.getHeader("Authorization");

    //  validate: 토큰 존재여부
    if (jwtTokenWithPrefix == null || !jwtTokenWithPrefix.startsWith(JwtProperties.PREFIX)) {
      chain.doFilter(request, response);
      return;
    }

    // validate: 토큰 (정상 사용자 여부)
    String jwtToken = jwtTokenWithPrefix.replace(JwtProperties.PREFIX, ""); // PREFIX 제거
    DecodedJWT decodedJWT = JWT.require(JwtProperties.ALGORITHM).build().verify(jwtToken);// verify가 정상적으로 되면, 인증된 토큰맞음.
    String email = decodedJWT.getClaim(JwtProperties.VALID_MEMBER_PROPERTY).asString();
    if (email != null) {
      Optional<Member> optionalMember = memberRepository.findByEmail(email);

      if (!optionalMember.isPresent()) {
        System.out.println("JwtAuthorizationFilter > 멤버 찾을 수 없음");
        chain.doFilter(request, response);
        return;
      }


      Member member = optionalMember.get();
      System.out.println("------ JwtAuthorizationFilter > member = " + member);
      //Authentication 객체를 강제로 만들기
      PrincipalDetails principalDetails = new PrincipalDetails(member);

      // JwtToken 서명이 정상일 경우, Authentication 객체를 만들어준다. (이미 인증된 member기 때문에, 이래도 괜찮다.)
      Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities()); // Param: principalDetails, 비밀번호, 권한

      // Security Session에 수동으로 신규 Authenticaion 객체 저장
      SecurityContextHolder.getContext().setAuthentication(authentication);

      // 다음 필터를 타게 만든다.
      chain.doFilter(request, response);
    }



  }

}
