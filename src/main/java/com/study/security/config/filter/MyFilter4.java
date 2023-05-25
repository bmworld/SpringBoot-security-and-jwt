package com.study.security.config.filter;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter4 implements Filter {
  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    System.out.println(" Filter4 실행! ");
    chain.doFilter(req, res);
  }
}
