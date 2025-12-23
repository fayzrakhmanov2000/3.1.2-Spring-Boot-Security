package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUser(long id);

    void deleteUser(long id);

    User findByUsername(String username);

    // новые методы, чтобы контроллер не знал про encoder
    void createUser(User user, String rawPassword, List<Long> roleIds);
    void updateUser(User user, String rawPassword, List<Long> roleIds);
}