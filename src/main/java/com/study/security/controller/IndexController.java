package com.study.security.controller;

import com.study.security.domain.Member;
import com.study.security.domain.RoleType;
import com.study.security.respotiroy.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping({"/", "/hello"})
    public String index() {
        return "index";
    }


    @GetMapping("/user")
    public @ResponseBody String user() {
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
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
    @PostAuthorize()
    @GetMapping("/data-info")
    public @ResponseBody String managerInfo() {
        return "매니저 정보";
    }
}
