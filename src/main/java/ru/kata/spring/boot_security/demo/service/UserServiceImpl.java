package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.HashSet;
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
        User user = userRepository.findById(id)
                .orElseThrow();

        user.getRoles().clear(); // 🔥 важно
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found username=" + username));
    }

    @Override
    @Transactional
    public void createUser(User user, String rawPassword, List<Long> roleIds) {

        user.setPassword(passwordEncoder.encode(rawPassword));

        Set<Role> roles = new HashSet<>();

        if (roleIds != null) {
            roles.addAll(roleRepository.findAllById(roleIds));
        }

        if (roles.isEmpty()) {
            Role defaultRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow();
            roles.add(defaultRole);
        }

        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUser(User user, String rawPassword, List<Long> roleIds) {

        User dbUser = userRepository.findById(user.getId())
                .orElseThrow();

        user.setPassword(
                rawPassword != null && !rawPassword.isBlank()
                        ? passwordEncoder.encode(rawPassword)
                        : dbUser.getPassword()
        );

        Set<Role> roles = new HashSet<>();
        if (roleIds != null) {
            roles.addAll(roleRepository.findAllById(roleIds));
        }
        user.setRoles(roles.isEmpty() ? dbUser.getRoles() : roles);

        userRepository.save(user);
    }

    private void validateUsername(User user) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username must not be blank");
        }
    }
}