package com.maco.followthebeat.v2.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @Email(message = "Invalid email")
    private String email;

}
