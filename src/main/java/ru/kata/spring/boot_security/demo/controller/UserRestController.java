package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.RoleDto;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserDto me(@AuthenticationPrincipal UserDetails userDetails) {
        User u = userService.findByUsername(userDetails.getUsername());
        return new UserDto(
                u.getId(),
                u.getName(),
                u.getSurname(),
                u.getAge(),
                u.getUsername(),
                u.getRoles().stream().map(r -> new RoleDto(r.getId(), r.getName())).collect(Collectors.toList())
        );
    }
}