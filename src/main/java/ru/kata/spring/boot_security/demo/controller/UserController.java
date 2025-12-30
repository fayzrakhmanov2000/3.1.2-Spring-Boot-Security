package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.stream.Collectors;

@Controller
public class UserController {

    @GetMapping("/user")
    public String userPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("authUserEmail", userDetails.getUsername());
        model.addAttribute("authUserRoles", userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.joining(" ")));
        model.addAttribute("currentPath", "/user");
        return "user";
    }
}