package com.alphago365.octopus.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ChangePasswordRequest {
    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 6)
    private String oldPassword;

    @NotBlank
    @Size(min = 6)
    private String newPassword;
}
