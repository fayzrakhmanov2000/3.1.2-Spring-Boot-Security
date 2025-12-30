package ru.kata.spring.boot_security.demo.dto;

import java.util.List;

public class UserDto {
    private Long id;
    private String name;
    private String surname;
    private int age;
    private String username;
    private List<RoleDto> roles;

    public UserDto() {}

    public UserDto(Long id, String name, String surname, int age, String username, List<RoleDto> roles) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.username = username;
        this.roles = roles;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public int getAge() { return age; }
    public String getUsername() { return username; }
    public List<RoleDto> getRoles() { return roles; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setAge(int age) { this.age = age; }
    public void setUsername(String username) { this.username = username; }
    public void setRoles(List<RoleDto> roles) { this.roles = roles; }
}