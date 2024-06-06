package ru.sixbeans.meetingengine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sixbeans.meetingengine.service.user.impl.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/avatars")
public class UserAvatarController {

    private final UserService userService;

    @GetMapping("/{subject}")
    public byte[] getUserAvatar(@PathVariable String subject) {
        return userService.getUserAvatar(subject);
    }
}
