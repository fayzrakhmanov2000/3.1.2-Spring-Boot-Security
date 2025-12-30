package ru.kata.spring.boot_security.demo.dto;

public class RoleDto {
    private Long id;
    private String name;

    public RoleDto() {}

    public RoleDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
}