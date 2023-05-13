package com.study.security.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED )
@ToString // 연관관계 매핑이 없으므로, 수업의 편의성을 위해 사용
public class Member extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="member_id")
    private Long id;
    private String username;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private RoleType role; // ADMIN, MANAGER, USER
    private LocalDateTime loginDate;
    private String provider;
    private String providerId;
}
