package ru.javacode.jsonview.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserDto {
    @Nullable
    private Long id;

    @NotBlank(message = "first name must not be empty")
    @Size(min = 3, max = 32, message = "First name must be between 3 and 32 characters long")
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message = "second name must not be empty")
    @Size(min = 3, max = 32, message = "Second name must be between 3 and 32 characters long")
    @JsonProperty("second_name")
    private String secondName;

    @Email(message = "Incorrect email format")
    private String email;
}
