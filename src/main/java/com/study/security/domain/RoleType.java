package com.study.security.domain;

public enum RoleType {
  USER, ADMIN, MANAGER;
  
  
  public static RoleType from(String role) {
    return String.valueOf(role) == "null" ? null : RoleType.valueOf(role.toUpperCase());
  }
  
  public static String to(RoleType type) {
    return type.toString();
    
  }
}
