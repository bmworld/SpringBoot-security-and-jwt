package com.study.security.dto;

import lombok.Data;

@Data
public class JoinRequestDto {
  private String username;
  private String password;
  private String email;
  private String role;
}
