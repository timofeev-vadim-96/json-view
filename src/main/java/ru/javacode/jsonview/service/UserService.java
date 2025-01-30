package ru.javacode.jsonview.service;

import ru.javacode.jsonview.controller.dto.UserDto;
import ru.javacode.jsonview.model.User;

import java.util.List;

public interface UserService {
    User getById(long id);

    List<User> getAll();

    User create(UserDto user);

    User update(UserDto user);

    void delete(long id);
}
