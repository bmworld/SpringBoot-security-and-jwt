package com.study.security.config.jwt;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.security.config.auth.PrincipalDetails;
import com.study.security.domain.Member;
import com.study.security.dto.JwtLoginDto;
import com.study.security.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

// Spring Security에 존재하는 Filter
// /login  Path에 userName, password 입력 시 작동하는 Filter
// formLogin.disable()했기 때문에 작동하지 않음
// 이것은, Security에서 다시 등록해줌으로서 재작동시킨다 => addFilter(new JwtAuthenticationFilter())

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  private final MemberService memberService;

  /**
   * <h1>로그인 시도 시, 실행되는 함수 이름.</h1>
   * <pre>
   *  - 동작시점: SecurityConfig에서 설정한 `/login` 요청
   *  - step1. username, password 요청받는 경우 attemptAuthentication 실행됨
   *  - step2. 위 정보로 로그인 시도 => authenticationManager 사용하여 로그인 시도 시, PrincipalDetailsService 실행됨.
   *  -      -> PrincipalDetailsService 실행 시, 내부 함수인 loadUserByUsername() 실행됨.
   *  - step3. PrincialDetails를 Session에 담는다.
   *         -> 왜 담나 ? 유저의 `권한` 관리를 위함.
   *  - step4. JWT Token 생성하여, Response로 응답해준다.
   *
   * </pre>
   */
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    System.out.println("----- JwtAuthenticationFilter: 로그인 시도 중.");
    try {
      /* ***** Request Param Parsing 방법 1 *****

      BufferedReader br = request.getReader();
      String input = null;
      // Request Parsing
      while ((input = br.readLine()) != null) {
        System.out.println("----- request.getReader() = " + input);
      }

      * */

      ////////////////////////////////////////////////////////////////////////////////////////////////


      // *****  Request Param Parsing 방법 2 *****
      ObjectMapper om = new ObjectMapper();
      JwtLoginDto dto = om.readValue(request.getInputStream(), JwtLoginDto.class);
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());

      // PrincipalDetailsService.loadUserByUsername() 실행
      Authentication authentication = authenticationManager.authenticate(authenticationToken);

      // Validation: 로그인 Email /PW 입력 불일치 시, loadUserByUsername() return null
      if (authentication == null) return null;

      /**
       * Authenticaion 객체를 attemptAuthentication() return 해서, spring `session`에 저장됨.
       * 왜 Return 하는가?
       * 편의성 ( security에  권한 관리를 대행시킴 )
       * ********** BUT **********
       * ***** JWT 사용할 경우, Session 사용할 필요 없음. *****
       * 이 함수 실행종료 후, successfulAuthentication 또는 unsuccessfulAuthentication 실행됨
       */
//      PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal(); //
////      System.out.println("---- 로그인 > 인증 성공 > principalDetails.getMember() = " + principalDetails.getMember());

      return authentication;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }


  /**
   *  <h1> attemptAuthentication 실행 후, 정상 인증 시, successfulAuthentication 실행됨. </h1>
   *  <pre>
   *    여기서 JWT token 응답해주면 됨.
   *  </pre>
   *  <pre>
   *    PROCESS
   *    1. Request: 유저 네임, 패스워드
   *    2. 인증완료 시, JWT Token or Server Session ID 생성 후, Client에 전달
   *    3. Client: JWT Token or Server Session ID와 함께 요청함
   *    4. Server: Client가 전달한 JWT Token or Session ID 유효성 검증 ( BY CUSTOM FILTER)
   *  </pre>
   */
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    System.out.println("----- successfulAuthentication > 인증 성공");

    // 인증 완료 시, authentication 객체를 받음
    PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
    Member member = principalDetails.getMember();

    // Hash 암호방식
    String jwtToken = JWT.create()
      .withSubject(principalDetails.getUsername())
      .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.DEFAULT_EXPIRED_TIME)) // 토큰 탈취를 대비하여, 길게잡지는 않는다.
      .withClaim("id", member.getId())
      .withClaim(JwtProperties.VALID_MEMBER_PROPERTY, member.getEmail()) // withClaim 에는 넣고싶은 필드를 추가할 수 있다.
      .sign(JwtProperties.ALGORITHM);

    String token = JwtProperties.PREFIX + jwtToken;
    System.out.println("----- 로그인 > 인증 성공 > 신규 생성 token = " + token);
    response.addHeader("Authorization", token);

    // 로그인 시간 기록
    memberService.login(member);


    super.successfulAuthentication(request, response, chain, authResult);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    System.out.println("----- unsuccessfulAuthentication > 인증 실패.");
    super.unsuccessfulAuthentication(request, response, failed);
  }
}
