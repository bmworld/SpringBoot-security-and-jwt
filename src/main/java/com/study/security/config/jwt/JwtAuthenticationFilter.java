package com.study.security.config.jwt;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Spring Security에 존재하는 Filter
// /login  Path에 userName, password 입력 시 작동하는 Filter
// formLogin.disable()했기 때문에 작동하지 않음
// 이것은, Security에서 다시 등록해줌으로서 재작동시킨다 => addFilter(new JwtAuthenticationFilter())
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
}
