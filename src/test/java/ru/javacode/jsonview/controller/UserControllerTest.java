package ru.javacode.jsonview.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javacode.jsonview.controller.dto.UserDto;
import ru.javacode.jsonview.exception.EntityNotFoundException;
import ru.javacode.jsonview.exception.IdNullPointerException;
import ru.javacode.jsonview.model.User;
import ru.javacode.jsonview.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UserController.class})
@DisplayName("Контроллер для работы с пользователями")
class UserControllerTest {
    private static User user;

    private static UserDto dto;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;


    @BeforeAll
    public static void init() {
        user = User.builder()
                .id(1L)
                .firstName("name")
                .secondName("secondName")
                .email("email@ya.ru")
                .build();

        dto = UserDto.builder()
                .id(1L)
                .firstName("name")
                .secondName("secondName")
                .email("email@ya.ru")
                .build();
    }

    @Test
    void get() throws Exception {
        final long id = user.getId();
        when(userService.getById(id)).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/user/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(user)));
    }

    @Test
    void getWhenNotFound() throws Exception {
        final long id = 111L;
        when(userService.getById(id)).thenThrow(EntityNotFoundException.class);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/user/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll() throws Exception {
        when(userService.getAll()).thenReturn(List.of(user));

        String body = mvc.perform(MockMvcRequestBuilders.get("/api/v1/user")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        List<User> users = mapper.readValue(body, new TypeReference<>() {
        });

        assertThat(users).usingRecursiveComparison()
                .ignoringFields("orders")
                .isEqualTo(List.of(user));
    }

    @Test
    void create() throws Exception {
        when(userService.create(dto)).thenReturn(user);

        String body = mvc.perform(MockMvcRequestBuilders.post("/api/v1/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        User responseBody = mapper.readValue(body, User.class);

        assertThat(responseBody).usingRecursiveComparison()
                .ignoringFields("orders")
                .isEqualTo(user);
    }

    @Test
    void update() throws Exception {
        when(userService.update(dto)).thenReturn(user);

        String body = mvc.perform(MockMvcRequestBuilders.put("/api/v1/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        User responseBody = mapper.readValue(body, User.class);

        assertThat(responseBody).usingRecursiveComparison()
                .ignoringFields("orders")
                .isEqualTo(user);
    }

    @Test
    void updateWhenNotFound() throws Exception {
        when(userService.update(dto)).thenThrow(EntityNotFoundException.class);

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateWhenIdIsNull() throws Exception {
        UserDto dtoWithoutId = UserDto.builder()
                .firstName("name")
                .secondName("secondName")
                .email("someEmail@mail.ru")
                .build();
        when(userService.update(dtoWithoutId)).thenThrow(IdNullPointerException.class);

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dtoWithoutId)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete() throws Exception {
        final long id = user.getId();
        doNothing().when(userService).delete(id);

        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/user/{id}", id))
                .andExpect(status().isOk());
    }
}