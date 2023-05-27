package com.study.security.config.jwt;

import com.auth0.jwt.algorithms.Algorithm;

public interface JwtProperties {

  String PREFIX = "Bearer "; //* '공백' 포함!

  String SECRET = "SOME_SECRET_STRING";

  String VALID_MEMBER_PROPERTY = "email";

  int SHORT_EXPIRED_TIME = 1000* 60 * 10; // millis * sec * min

  int DEFAULT_EXPIRED_TIME = 1000* 60 * 60 * 2; // millis * sec * min * hour

  Algorithm ALGORITHM = Algorithm.HMAC512(JwtProperties.SECRET);
}
