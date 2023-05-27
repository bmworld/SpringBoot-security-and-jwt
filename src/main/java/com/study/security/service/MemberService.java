package com.study.security.service;

import com.study.security.domain.Member;
import com.study.security.respotiroy.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

  private final MemberRepository memberRepository;

  @Transactional
  public void login(Member member) {
    member.setLoginDate(LocalDateTime.now());
    memberRepository.save(member);
  }


}
