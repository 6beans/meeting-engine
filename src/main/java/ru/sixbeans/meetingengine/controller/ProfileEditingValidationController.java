package ru.sixbeans.meetingengine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sixbeans.meetingengine.service.user.impl.UserProfileService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile/validation")
public class ProfileEditingValidationController {

    private final UserProfileService userProfileService;

    @GetMapping("/emails/{email}")
    public ResponseEntity<Void> emailValidation(@PathVariable String email,
                                                @AuthenticationPrincipal OidcUser principal) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
            return ResponseEntity.badRequest().build();
        if (!userProfileService.isEmailUnique(principal.getSubject(), email))
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/usernames/{username}")
    public ResponseEntity<Void> usernameValidation(@PathVariable String username,
                                                   @AuthenticationPrincipal OidcUser principal) {
        if (!username.matches("^[a-zA-Z]\\w{2,16}$"))
            return ResponseEntity.badRequest().build();
        if (!userProfileService.isUsernameUnique(principal.getSubject(), username))
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        return ResponseEntity.ok().build();
    }
}
