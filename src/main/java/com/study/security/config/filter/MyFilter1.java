package com.study.security.config.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <h2>Token Process</h2>
 * <pre>
 *   1. Id, PW 정상 입력 시, 로그인 완료되면, Token을 만들어준다.
 *   2. 매 요청마다, header에 Authorization에 value값으로 Token을 전달받는다.
 *   3. 위 2번의 토큰이 내가 만든 Token이 맞는지 검증한다. 끝.
 * </pre>
 */
public class MyFilter1 implements Filter {
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request; // DownCasting 해준다.
    HttpServletResponse res = (HttpServletResponse) response; // DownCasting 해준다.

    System.out.println(" Filter1 실행! ");
//    System.out.println("------ req.getParameterNames() "+req.getParameterNames().toString());

    // Token
    String token = "bm";
    if (req.getMethod().equals("POST")) {
      String headerAuth = req.getHeader("Authorization");
      System.out.println("----- headerAuth = " + headerAuth);
      if (headerAuth !=null && headerAuth.equals(token)) {
        // * Filter > doFilter() : req, res 넘겨줘야, Application 요청이 다음 필터, 또는 Security로 넘어간다.
        chain.doFilter(req, res);
      } else {
        PrintWriter out = res.getWriter();
        out.println("Invalid.");
      }
    }
  }
}
