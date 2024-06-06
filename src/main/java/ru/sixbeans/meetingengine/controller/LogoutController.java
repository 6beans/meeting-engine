package ru.sixbeans.meetingengine.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Configuration
@RequestMapping("/logout")
public class LogoutController {

    @PostMapping
    public void logout() {
        // Keycloak logout
    }
}
