package com.study.security.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Spring Security에 존재하는 Filter
// /login  Path에 userName, password 입력 시 작동하는 Filter
// formLogin.disable()했기 때문에 작동하지 않음
// 이것은, Security에서 다시 등록해줌으로서 재작동시킨다 => addFilter(new JwtAuthenticationFilter())

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private final AuthenticationManager authenticationManager;


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
    return super.attemptAuthentication(request, response);
  }
}
