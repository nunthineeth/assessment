package com.kbtg.bootcamp.posttest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserDto(
        @NotNull
        @Size(min = 4, max = 12, message = "username length should be 4-12 characters")
        String username,
        @NotNull
        String password,
        @NotNull
        @Email
        String email,
        @NotNull
        String firstname,
        @NotNull
        String lastname,
        @NotNull
        String roleCode
) {


}
