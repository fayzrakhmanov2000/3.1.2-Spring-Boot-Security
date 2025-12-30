package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.RoleDto;
import ru.kata.spring.boot_security.demo.dto.UserCreateUpdateRequest;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.mapper.RoleMapper;
import ru.kata.spring.boot_security.demo.mapper.UserMapper;
import ru.kata.spring.boot_security.demo.mapper.UserRequestMapper;
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

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final UserRequestMapper userRequestMapper;

    public AdminRestController(UserService userService,
                               RoleService roleService,
                               UserMapper userMapper,
                               RoleMapper roleMapper,
                               UserRequestMapper userRequestMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.userRequestMapper = userRequestMapper;
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/roles")
    public List<RoleDto> getAllRoles() {
        return roleService.getAllRoles().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@RequestBody UserCreateUpdateRequest req) {
        User u = userRequestMapper.toEntity(req);

        userService.createUser(u, req.getRawPassword(), req.getRoleIds());

        User created = userService.findByUsername(req.getUsername());
        return ResponseEntity
                .created(URI.create("/api/admin/users/" + created.getId()))
                .body(userMapper.toDto(created));
    }

    @PutMapping("/users/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserCreateUpdateRequest req) {
        User u = userRequestMapper.toEntityWithId(id, req);

        userService.updateUser(u, req.getRawPassword(), req.getRoleIds());

        return userMapper.toDto(userService.getUser(id));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}