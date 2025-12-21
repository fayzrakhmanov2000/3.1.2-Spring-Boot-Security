package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // ===================== ADMIN =====================

    @GetMapping("/admin")
    public String showAllUsers(Model model) {
        model.addAttribute("allUsers", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/admin/addNewUser")
    public String addNewUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("formAction", "/admin/saveUser");
        model.addAttribute("isNew", true);
        return "user-info";
    }

    @PostMapping("/admin/saveUser")
    public String saveUser(@ModelAttribute("user") User user,
                           @RequestParam("rawPassword") String rawPassword) {

        // username и пароль должны быть НЕ null
        user.setPassword(passwordEncoder.encode(rawPassword));
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/updateInfo")
    public String updateInfo(@RequestParam("userId") Long id, Model model) {
        User user = userService.getUser(id);
        model.addAttribute("user", user);
        model.addAttribute("formAction", "/admin/updateUser");
        model.addAttribute("isNew", false);
        return "user-info";
    }

    @PostMapping("/admin/updateUser")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "rawPassword", required = false) String rawPassword) {

        User dbUser = userService.getUser(user.getId());

        // если пароль не ввели — оставляем старый
        if (rawPassword != null && !rawPassword.isBlank()) {
            user.setPassword(passwordEncoder.encode(rawPassword));
        } else {
            user.setPassword(dbUser.getPassword());
        }

        // если username вдруг не пришёл из формы — не даём затереть null-ом
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            user.setUsername(dbUser.getUsername());
        }

        userService.updateUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/deleteUser")
    public String deleteUser(@RequestParam("userId") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    // ===================== USER =====================

    @GetMapping("/user")
    public String userPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // достаём твоего User из БД по username
        User user = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("user", user);
        return "user";
    }
}