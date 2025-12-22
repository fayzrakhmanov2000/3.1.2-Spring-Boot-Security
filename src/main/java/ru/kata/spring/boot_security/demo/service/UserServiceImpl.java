package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found id=" + id));
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found username=" + username));
    }

    @Override
    @Transactional
    public void createUser(User user, String rawPassword) {
        validateUsername(user);
        user.setPassword(passwordEncoder.encode(rawPassword));

        // если роли не пришли из формы — выдадим ROLE_USER по умолчанию
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role roleUser = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new IllegalStateException("ROLE_USER not found in DB"));
            user.setRoles(Set.of(roleUser));
        }

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUser(User user, String rawPassword) {
        User dbUser = getUser(user.getId());

        // username нельзя затирать null/blank
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            user.setUsername(dbUser.getUsername());
        }

        // пароль: если не ввели — оставляем старый
        if (rawPassword != null && !rawPassword.isBlank()) {
            user.setPassword(passwordEncoder.encode(rawPassword));
        } else {
            user.setPassword(dbUser.getPassword());
        }

        // роли: если форма не прислала — оставляем прежние
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(dbUser.getRoles());
        }

        userRepository.save(user);
    }

    private void validateUsername(User user) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username must not be blank");
        }
    }
}