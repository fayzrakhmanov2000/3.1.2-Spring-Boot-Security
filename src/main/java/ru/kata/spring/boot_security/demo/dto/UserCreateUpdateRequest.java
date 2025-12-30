package ru.kata.spring.boot_security.demo.dto;

import java.util.List;

public class UserCreateUpdateRequest {
    private Long id;
    private String name;
    private String surname;
    private int age;
    private String username;

    // для create обязателен, для update может быть пустым (оставить старый)
    private String rawPassword;

    private List<Long> roleIds;

    public UserCreateUpdateRequest() {}

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public int getAge() { return age; }
    public String getUsername() { return username; }
    public String getRawPassword() { return rawPassword; }
    public List<Long> getRoleIds() { return roleIds; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setAge(int age) { this.age = age; }
    public void setUsername(String username) { this.username = username; }
    public void setRawPassword(String rawPassword) { this.rawPassword = rawPassword; }
    public void setRoleIds(List<Long> roleIds) { this.roleIds = roleIds; }
}