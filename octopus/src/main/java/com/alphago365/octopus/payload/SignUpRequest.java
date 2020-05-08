package com.alphago365.octopus.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SignUpRequest {
    @NotBlank
    private String username;

    @Email
    private String email;

    @NotBlank
    private String cellphone;

    @NotBlank
    @Size(min = 6)
    private String password;
}
