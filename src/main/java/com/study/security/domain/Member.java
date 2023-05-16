package com.study.security.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.fasterxml.jackson.databind.cfg.CoercionInputShape.Array;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString // 연관관계 매핑이 없으므로, 수업의 편의성을 위해 사용
public class Member extends BaseEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;
  private String username;
  
  private String password;
  
  private String email;
  
  private String roles; // [ADMIN, MANAGER, USER] 쉼표로 구분
  
  private LocalDateTime loginDate;
  
  private String provider;
  
  private String providerId;
  
  public List<String> getRoleList() {
    return roles.length() > 0
      ? Arrays.asList(this.roles.split(","))
      : new ArrayList<>();
  }
}
