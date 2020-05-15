package com.alphago365.octopus.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SignInRequest {
    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 6)
    private String password;
}
