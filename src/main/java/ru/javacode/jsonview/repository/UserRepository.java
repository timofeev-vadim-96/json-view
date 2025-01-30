package ru.javacode.jsonview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javacode.jsonview.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
