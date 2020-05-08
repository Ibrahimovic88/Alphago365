package com.alphago365.octopus.payload;

import lombok.Data;

@Data
public class AppResponse {
    private boolean success;
    private String message;

    public AppResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
