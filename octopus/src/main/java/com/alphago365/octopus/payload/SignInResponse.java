package com.alphago365.octopus.payload;

import lombok.Data;

@Data
public class SignInResponse {
    private String tokenType = "Bearer";
    private final String accessToken;

    public SignInResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
