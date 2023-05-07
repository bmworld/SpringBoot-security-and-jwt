package com.study.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // Spring Security Filter가  Spring Filter Chain에 등록됨
/**
 * @securedEnabled @Secured(...) Annotation 사용하여 접근권한 설정 가능
 * @prePostEnabled @PreAuthorize Annotation 사용하여, Annotation이 걸린 Method의 실행 직전에 실행됨
 *
 */
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true )
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     * BCrypt 암호화를 Bean 등록하여, 회원가입 시 사용
     */
    @Bean
    public BCryptPasswordEncoder encodePw() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // csrf: Cross site Request forgery //
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() // 인증만 되면, 접근가능하게 설정함.
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login") // login 주소가 호출될 경우, Security가 intercept해서 대신 Login처리함.
                .defaultSuccessUrl("/")
        ;
    }
}
