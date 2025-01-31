package ru.javacode.jsonview.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.javacode.jsonview.controller.dto.UserDto;
import ru.javacode.jsonview.model.User;
import ru.javacode.jsonview.service.UserService;
import ru.javacode.jsonview.view.Views;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/api/v1/user/{id}")
    @JsonView(Views.UserDetails.class)
    public ResponseEntity<User> get(@PathVariable(name = "id") long id) {
        User user = userService.getById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/api/v1/user")
    @JsonView(Views.UserSummary.class)
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userService.getAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/api/v1/user")
    @JsonView(Views.UserSummary.class)
    public ResponseEntity<User> create(@Valid @RequestBody UserDto dto) {
        User created = userService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/api/v1/user")
    @JsonView(Views.UserSummary.class)
    public ResponseEntity<User> update(@Valid @RequestBody UserDto dto) {
        User updated = userService.update(dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/api/v1/user/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
