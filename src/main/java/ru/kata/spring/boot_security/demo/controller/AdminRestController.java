package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.RoleDto;
import ru.kata.spring.boot_security.demo.dto.UserCreateUpdateRequest;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRestController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers().stream().map(this::toDto).collect(Collectors.toList());
    }

    @GetMapping("/roles")
    public List<RoleDto> getAllRoles() {
        return roleService.getAllRoles().stream()
                .map(r -> new RoleDto(r.getId(), r.getName()))
                .collect(Collectors.toList());
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@RequestBody UserCreateUpdateRequest req) {
        User u = new User();
        u.setName(req.getName());
        u.setSurname(req.getSurname());
        u.setAge(req.getAge());
        u.setUsername(req.getUsername());

        userService.createUser(u, req.getRawPassword(), req.getRoleIds());

        User created = userService.findByUsername(req.getUsername());
        return ResponseEntity
                .created(URI.create("/api/admin/users/" + created.getId()))
                .body(toDto(created));
    }

    @PutMapping("/users/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserCreateUpdateRequest req) {
        User u = new User();
        u.setId(id);
        u.setName(req.getName());
        u.setSurname(req.getSurname());
        u.setAge(req.getAge());
        u.setUsername(req.getUsername());

        userService.updateUser(u, req.getRawPassword(), req.getRoleIds());
        return toDto(userService.getUser(id));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    private UserDto toDto(User u) {
        List<RoleDto> roles = u.getRoles().stream()
                .map(r -> new RoleDto(r.getId(), r.getName()))
                .collect(Collectors.toList());

        return new UserDto(u.getId(), u.getName(), u.getSurname(), u.getAge(), u.getUsername(), roles);
    }
}