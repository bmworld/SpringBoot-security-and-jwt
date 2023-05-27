package com.study.security.config.auth;

import com.study.security.domain.Member;
import com.study.security.respotiroy.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <h1>작동방식</h1>
 * <pre>
 * Security Config => LoginProcessingUrl("/login");
 * 위 요청 시, 자동으로 UserDetailsService 타입으로 IoC된 loadUserByUsername 함수 실행.
 *
 * return 값은 다음과 같은 구조로 객체 내에 저장된다
 *
 * => SecuritySession(Authentication(UserDetails))
 * </pre>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    //

    /**
     * <h1>PrincipalDetailsService 호출 시, `@AuthenticationPrincipal` 어노테이션 만들이진다.</h1>
     * - Security 내부 세션 <br/>
     */

    /**
      thymeLeaf <form> 태그 사용 시, loadUserByUsername에 전달되는 param이름과 매칭되는 것을 유의.
    */

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        System.out.println("----- PrincipalDetailsService > username = " + username);
//        // 파라미터는 username이 기본이지만, form 에서 username이 아닌 다른 이름으로 명명할 경우, SecurityConfig에서 해당 parameter 이름을 변경해줘야하는 귀찮음이 발생함.
//        Optional<Member> optionalMember = memberRepository.findByUsername(username);
//        if (optionalMember.isPresent()) {
//            Member member = optionalMember.get();
//            member.setLoginDate(LocalDateTime.now());
//            memberRepository.save(member);
//            return new PrincipalDetails(member);
//        }
//        return null;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        System.out.println("----- PrincipalDetailsService > username = " + username);
//        // 파라미터 =>
//        Optional<Member> optionalMember = memberRepository.findByUsername(username);
//        if (optionalMember.isPresent()) {
//            Member member = optionalMember.get();
//            member.setLoginDate(LocalDateTime.now());
//            memberRepository.save(member);
//            return new PrincipalDetails(member);
//        }
//        return null;
//    }




    /// ###################################################################################################
    /// ###################################################################################################
    /**
      API 방식으로 JWT 사용 시, new UsernamePasswordAuthenticationToken(firstParam, paswrodParam) <-- 해당 firstParam 값이 전달된다.
     */
    @Override
    public UserDetails loadUserByUsername(String firstParam) throws UsernameNotFoundException {
        System.out.println("----- PrincipalDetailsService > email = " + firstParam);
        Optional<Member> optionalMember = memberRepository.findByEmail(firstParam);

        // Validation
        if (!optionalMember.isPresent()) return null;

        //
        Member member = optionalMember.get();
        return new PrincipalDetails(member);

    }
}
