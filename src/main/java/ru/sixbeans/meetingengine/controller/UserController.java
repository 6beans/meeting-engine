package ru.sixbeans.meetingengine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ru.sixbeans.meetingengine.model.UserData;
import ru.sixbeans.meetingengine.service.user.impl.UserService;


@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public String getAllUsers(@AuthenticationPrincipal OidcUser principal, Model model) {
        model.addAttribute("authorizedUser", userService.getUserByPrincipal(principal));
        model.addAttribute("users", userService.getAllUsers());
        return "home";
    }

    @GetMapping("/users/{userIdFromUrl}")
    public String getUser(@AuthenticationPrincipal OidcUser principal, @PathVariable long userIdFromUrl, Model model) {
        UserData authorizedUser = userService.getUserByPrincipal(principal);
        UserData userFromURL = userService.getUserById(authorizedUser.id(), userIdFromUrl);

        model.addAttribute("user", userFromURL);
        model.addAttribute("userPersonalInfo", userService.getUserPersonalInfoById(userIdFromUrl));
        return "user";
    }

    @GetMapping("/subs")
    public String getSubscribersAndSubscriptions(@AuthenticationPrincipal OidcUser principal, Model model) {
        UserData authorizedUser = userService.getUserByPrincipal(principal);

        model.addAttribute("subscribers", userService.findAllUserSubscribers(authorizedUser.id()));
        model.addAttribute("subscriptions", userService.findAllUserSubscriptions(authorizedUser.id()));
        return "subs";
    }

    @PostMapping("/subscription/{friendIdFromUrl}")
    public void subscribe(@AuthenticationPrincipal OidcUser principal, @PathVariable long friendIdFromUrl) {
        UserData authorizedUser = userService.getUserByPrincipal(principal);
        userService.subscribe(authorizedUser.id(), friendIdFromUrl);
    }

    @DeleteMapping("/subscription/{friendIdFromUrl}")
    public void unsubscribe(@AuthenticationPrincipal OidcUser principal, @PathVariable long friendIdFromUrl) {
        UserData authorizedUser = userService.getUserByPrincipal(principal);
        userService.unsubscribe(authorizedUser.id(), friendIdFromUrl);
    }
}