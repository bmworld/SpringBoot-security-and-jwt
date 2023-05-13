package com.study.security.controller;

import com.study.security.config.auth.CurrentUser;
import com.study.security.config.auth.PrincipalDetails;
import com.study.security.domain.Member;
import com.study.security.respotiroy.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;



    /**
     * <h1>Security - Member 정보 가져오기</h1>
     * @param authentication Security 세션정보를 받을 수 있다. (Authentication DI(의존성 주입) 시, Principal 접근 가능)
     * @param userDetails Annotation 사용한 유저정보 가져오기 (PrincipalDetails은 UserDetails을 상속받았기 때문에 유저정보가 있음)
     */
    @GetMapping("/session/security")
    public @ResponseBody String loginInfo_security(
            Authentication authentication,
            @AuthenticationPrincipal PrincipalDetails userDetails
            ) {

        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();

        System.out.println("principal.getMember() = " + principal.getMember());
        System.out.println("userDetails.getMember() = " + userDetails.getMember());

        return "Security - 세션 정보 확인";
    }


    /**
     * <h1>Oauth - Member 정보 가져오기</h1>
     * @param authentication Oauth2User로 DownCasting 후, 유저정보 가져올 수 있음
     * @param oauth2UserBy_AuthenticationPrincipal  Annotation 사용한 유저정보 가져오기
     */
    @GetMapping("/session/oauth")
    public @ResponseBody String loginInfo_oauth(
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User oauth2UserBy_AuthenticationPrincipal
    ) {

        OAuth2User oauth2UserByAuthentication = (OAuth2User) authentication.getPrincipal();
        System.out.println("----- Authentication > getPrincipal() > getAttributes() = " + oauth2UserByAuthentication.getAttributes());
        System.out.println("----- @AuthenticationPrincipal OAuth2User > getAttributes() = " + oauth2UserBy_AuthenticationPrincipal.getAttributes());
        return "OAuth2 - 세션 정보 확인";
    }
    @GetMapping({"/", "/hello"})
    public String index() {
        return "index";
    }


    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("----- principalDetails.getMember() = " + principalDetails.getMember());
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(@CurrentUser Member member) {
        System.out.println("@CurrentUser > member = " + member);
        return "admin";
    }


    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }


    @GetMapping("/joinForm")
    public String joinForm(Member member) {
        return "joinForm";
    }




    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }

    /**
     * Password 암호화하지 않을 경우, Security 를 사용한 Login 불가
     */
    @PostMapping("/join")
    public String join(Member member) {
        System.out.println("----- join > member = " + member);
//        member.setRole(RoleType.USER);
        String rawPW = member.getPassword();
        String encPW = bCryptPasswordEncoder.encode(rawPW);
        member.setPassword(encPW);
        memberRepository.save(member);
        return "redirect:/loginForm";
    }

    @GetMapping("/joinProc")
    public String joinProc() {
        return "회원가입 완료";
    }



    @Secured("ROLE_ADMIN")
    @GetMapping("/admin-info")
    public @ResponseBody String adminInfo() {
        return " 관리자 개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data-info")
    public @ResponseBody String managerInfo() {
        return "매니저 정보";
    }
}
