package com.alphago365.octopus;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestartController {

    @PostMapping(value = "/restart")
    @PreAuthorize("hasRole('ADMIN')")
    public void restart() {
        OctopusApplication.restart();
    }
}