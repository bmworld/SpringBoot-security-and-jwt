package com.study.security.dto;

import lombok.Data;

@Data
public class JwtLoginDto {
  private String email;
  private String password;
}
