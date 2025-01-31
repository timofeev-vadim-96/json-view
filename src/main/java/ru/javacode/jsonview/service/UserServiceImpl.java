package ru.javacode.jsonview.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javacode.jsonview.controller.dto.UserDto;
import ru.javacode.jsonview.exception.EntityNotFoundException;
import ru.javacode.jsonview.exception.IdNullPointerException;
import ru.javacode.jsonview.model.User;
import ru.javacode.jsonview.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    @Cacheable(value = "userById", key = "#id")
    public User getById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id = %d is not found".formatted(id)));

        Hibernate.initialize(user.getOrders());

        return user;
    }

    @Transactional(readOnly = true)
    @Override
    @Cacheable(value = "users")
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    @CacheEvict(value = "users", allEntries = true)
    public User create(UserDto dto) {
        User user = User.builder()
                .firstName(dto.getFirstName())
                .secondName(dto.getSecondName())
                .email(dto.getEmail())
                .build();

        return userRepository.save(user);
    }

    @Transactional
    @Override
    @Caching(evict = {
            @CacheEvict(value = "users", allEntries = true),
            @CacheEvict(value = "userById", key = "#dto.id", beforeInvocation = false)})
    public User update(UserDto dto) {
        Long id = dto.getId();
        if (id == null) {
            throw new IdNullPointerException("User id must not be null");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id = %d is not found".formatted(id)));
        user.setFirstName(dto.getFirstName());
        user.setSecondName(dto.getSecondName());
        user.setEmail(dto.getEmail());

        return userRepository.save(user);
    }

    @Transactional
    @Override
    @Caching(evict = {
            @CacheEvict(value = "users", allEntries = true),
            @CacheEvict(value = "userById", key = "#id")})
    public void delete(long id) {
        userRepository.deleteById(id);
    }
}
