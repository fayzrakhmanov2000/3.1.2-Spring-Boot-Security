package ru.kata.spring.boot_security.demo.mapper;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.UserCreateUpdateRequest;
import ru.kata.spring.boot_security.demo.model.User;

@Component
public class UserRequestMapper {

    public User toEntity(UserCreateUpdateRequest req) {
        if (req == null) return null;

        User u = new User();
        u.setId(req.getId()); // может быть null на create
        u.setName(req.getName());
        u.setSurname(req.getSurname());
        u.setAge(req.getAge());
        u.setUsername(req.getUsername());
        // password НЕ ставим здесь: rawPassword отдельно обрабатывает сервис
        return u;
    }

    public User toEntityWithId(Long id, UserCreateUpdateRequest req) {
        User u = toEntity(req);
        if (u != null) u.setId(id);
        return u;
    }
}