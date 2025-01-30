package ru.javacode.jsonview.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javacode.jsonview.controller.dto.UserDto;
import ru.javacode.jsonview.exception.EntityNotFoundException;
import ru.javacode.jsonview.exception.IdNullPointerException;
import ru.javacode.jsonview.model.User;
import ru.javacode.jsonview.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Сервис для работы с пользователями")
@Transactional(propagation = Propagation.NEVER)
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private static User user;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<User> userCaptor;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository);
    }

    @BeforeAll
    public static void init() {
        user = User.builder()
                .id(1L)
                .firstName("name")
                .secondName("secondName")
                .email("email@ya.ru")
                .build();
    }

    @Test
    void getById() {
        final long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User actual = userService.getById(id);

        assertThat(actual).isNotNull().usingRecursiveComparison().isEqualTo(user);
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void getAllThrowsWhenUserNotFound() {
        final long notExistingId = 101L;
        when(userRepository.findById(notExistingId)).thenReturn(Optional.empty());

        assertThrowsExactly(EntityNotFoundException.class, () -> userService.getById(notExistingId));
        verify(userRepository, times(1)).findById(notExistingId);
    }

    @Test
    void getAll() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userService.getAll();

        assertThat(users).isNotNull().isNotEmpty();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void create() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserDto dto = UserDto.builder()
                .firstName(user.getFirstName())
                .secondName(user.getSecondName())
                .email(user.getEmail())
                .build();

        User created = userService.create(dto);

        assertThat(created).isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);
        verify(userRepository).save(userCaptor.capture());
        User userCaptorValue = userCaptor.getValue();
        assertThat(userCaptorValue).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(user);
    }

    @Test
    void update() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        UserDto dto = UserDto.builder()
                .id(user.getId())
                .firstName("changedName")
                .secondName("changedSecondName")
                .email("anotherEmail@ya.ru")
                .build();
        User forReturn = User.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .secondName(dto.getSecondName())
                .email(dto.getEmail())
                .build();
        when(userRepository.save(any(User.class))).thenReturn(forReturn);

        User updated = userService.update(dto);

        assertThat(updated).isNotNull()
                .hasFieldOrPropertyWithValue("firstName", dto.getFirstName())
                .hasFieldOrPropertyWithValue("secondName", dto.getSecondName())
                .hasFieldOrPropertyWithValue("email", dto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateThrowsWhenIdIsNull() {
        UserDto dto = UserDto.builder()
                .build();

        assertThrowsExactly(IdNullPointerException.class, () -> userService.update(dto));
    }

    @Test
    void updateThrowsWhenUserNotFound() {
        final long id = 111L;
        UserDto dto = UserDto.builder()
                .id(id)
                .build();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrowsExactly(EntityNotFoundException.class, () -> userService.update(dto));
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void delete() {
        final long id = 1L;
        doNothing().when(userRepository).deleteById(id);

        userService.delete(id);

        verify(userRepository).deleteById(id);
    }
}