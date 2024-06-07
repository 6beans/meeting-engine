package ru.sixbeans.meetingengine.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sixbeans.meetingengine.model.UserData;
import ru.sixbeans.meetingengine.service.tag.impl.TagService;
import ru.sixbeans.meetingengine.service.user.impl.UserProfileService;
import ru.sixbeans.meetingengine.service.user.impl.UserService;

import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileEditingController {

    private final UserService userService;
    private final UserProfileService userProfileService;
    private final Keycloak keycloak;
    private final TagService tagService;

    @GetMapping
    public String getProfile(@AuthenticationPrincipal OidcUser principal, Model model) {
        UsersResource usersResource = keycloak.realm("engine").users();
        UserResource userResource = usersResource.get(principal.getSubject());
        UserRepresentation userRepresentation = userResource.toRepresentation();
        byte[] userAvatar = userService.getUserAvatar(principal.getSubject());
        String avatarBase64 = Base64.getEncoder().encodeToString(userAvatar);
        model.addAttribute("tags", String.join(",", tagService.findAllUserTags(principal.getSubject())));
        model.addAttribute("user", userRepresentation);
        model.addAttribute("avatar", avatarBase64);
        return "edit-profile";
    }

    @SneakyThrows
    @PostMapping
    public String updateProfile(
            @ModelAttribute UserData userData,
            @AuthenticationPrincipal OidcUser principal,
            @RequestParam(value = "newPassword", required = false) String newPassword,
            @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
            @RequestParam(value = "image", required = false) MultipartFile avatar,
            @RequestParam(value = "tags", required = false) List<String> tags) {
        String subject = principal.getSubject();
        String email = principal.getEmail();
        tagService.updateUserTags(subject, tags);
        if (!userProfileService.isUsernameUnique(subject, userData.userName()))
            return "redirect:/profile?status=usernameNotUnique";
        if (!userProfileService.isEmailUnique(subject, userData.email()))
            return "redirect:/profile?status=emailNotUnique";
        if (newPassword != null && !newPassword.isBlank() && newPassword.equals(confirmPassword))
            userProfileService.updateUserPassword(subject, newPassword);
        userProfileService.updateUserProfile(subject, userData);
        if (!avatar.isEmpty())
            userProfileService.updateUserAvatar(subject, avatar.getBytes());
        if (!email.equals(userData.email()))
            return "redirect:/profile?status=emailUpdated";
        return "redirect:/profile?status=success";
    }
}
