package com.study.security.config;

import com.study.security.config.filter.MyFilter3;
import com.study.security.config.filter.MyFilter4;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

  /**
   * <h1>Custom Security Filter를 만들어보자</h1>
   */
  @Bean
  public FilterRegistrationBean<MyFilter3> filter3() {
    FilterRegistrationBean<MyFilter3> bean = new FilterRegistrationBean<>(new MyFilter3());
    bean.addUrlPatterns("/*"); //
    bean.setOrder(0); // Order 번호가 낮을 수록 , 먼저 실행됨
    return bean;
  }

  @Bean
  public FilterRegistrationBean<MyFilter4> filter4() {
    FilterRegistrationBean<MyFilter4> bean = new FilterRegistrationBean<>(new MyFilter4());
    bean.addUrlPatterns("/*"); //
    bean.setOrder(1); // Order 번호가 낮을 수록 , 먼저 실행됨
    return bean;
  }

}
