package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String showAdminPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("allUsers", userService.getAllUsers());
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("newUser", new User());

        model.addAttribute("authUserEmail", userDetails.getUsername());
        model.addAttribute("authUserRoles", userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.joining(" ")));

        model.addAttribute("currentPath", "/admin");

        return "admin";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("newUser") User user,
                           @RequestParam("rawPassword") String rawPassword,
                           @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {
        userService.createUser(user, rawPassword, roleIds);
        return "redirect:/admin";
    }

    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute User user,
                             @RequestParam(value = "rawPassword", required = false) String rawPassword,
                             @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {
        userService.updateUser(user, rawPassword, roleIds);
        return "redirect:/admin";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}