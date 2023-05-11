package com.study.security.config;


import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class BcryptPasswordEncoderInitializer {
    /**
     * BCrypt 암호화를 Bean 등록하여, 회원가입 시 사용
     */
    @Bean
    public BCryptPasswordEncoder encodePw() {
        return new BCryptPasswordEncoder();
    }
}
