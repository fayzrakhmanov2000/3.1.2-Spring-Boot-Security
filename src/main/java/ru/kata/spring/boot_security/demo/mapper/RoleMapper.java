package ru.kata.spring.boot_security.demo.mapper;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.RoleDto;
import ru.kata.spring.boot_security.demo.model.Role;

@Component
public class RoleMapper {

    public RoleDto toDto(Role role) {
        if (role == null) return null;
        return new RoleDto(role.getId(), role.getName());
    }
}