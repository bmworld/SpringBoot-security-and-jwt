package com.study.security.config;

import com.study.security.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity // Spring Security Filter가  Spring Filter Chain에 등록됨
/**
 * @securedEnabled @Secured(...) Annotation 사용하여 접근권한 설정 가능
 * @prePostEnabled @PreAuthorize Annotation 사용하여, Annotation이 걸린 Method의 실행 직전에 실행됨
 *
 */
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true )
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalOauth2UserService principalOauth2UserService;
    
    private final CorsFilter corsFilter;

    /**
     * <h1>Session Ver.</h1>
     * <h3>Oauth - Process</h3>
     * <pre>
     *     1. 코드 받기 (인증)
     *     2. Access Token
     *     3. 사용자 Profile 받기
     * </pre>
     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable(); // csrf: Cross site Request forgery //
//        http.authorizeRequests()
//                .antMatchers("/user/**").authenticated() // 인증만 되면, 접근가능하게 설정함.
//                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
//                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
//                .anyRequest().permitAll()
//                .and()
//                .formLogin()
//                .loginPage("/loginForm")
//                .loginProcessingUrl("/login") // login 주소가 호출될 경우, Security가 intercept해서 대신 Login처리함.
//                .defaultSuccessUrl("/")
//                .and()
//                // OAUTH2 MAPPING
//                .oauth2Login()
//                .loginPage("/loginForm")
//                .userInfoEndpoint()
//                .userService(principalOauth2UserService) // 구글 로그인 완료 후 => Access Token, 사용자 profile 넣기)
//        ;
//    }
    
    
    /**
     * <h1>JWT Ver.</h1>
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
          .addFilter(corsFilter) // 모든 HTTP Request에 Filter => 시큐리테 필터레 등록하여 인증 (@CrossOrigin Annotation은 인증 불가)
          .formLogin().disable()
          .httpBasic().disable()
          .authorizeRequests()
          .antMatchers("/user/**").authenticated() // 인증만 되면, 접근가능하게 설정함.
          .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
          .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
          .antMatchers("/api/v1/user/**").authenticated() // 인증만 되면, 접근가능하게 설정함.
          .antMatchers("/api/v1/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
          .antMatchers("/api/v1/admin/**").access("hasRole('ROLE_ADMIN')")
          .anyRequest().permitAll()
        ;
        
    }
}
