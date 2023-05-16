package com.study.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true); // 본 서버가 응답 시, json을 `javascript` 로 처리가능여부(ajax 등)
    config.addAllowedOrigin("*"); // 모든 `ip` 에 대한 응답 허용
    config.addAllowedHeader("*"); // 모든 `header`에 대한 응답 허용
    config.addAllowedMethod("*"); // 모든 Http Request Method 허용 [POST, GET, PUT, DELETE, PATCH]
    source.registerCorsConfiguration("/api/**", config);
    return new CorsFilter(source);
  }
}
