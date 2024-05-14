package ru.sixbeans.meetingengine.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sixbeans.meetingengine.service.user.impl.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/avatars")
public class UserAvatarController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<byte[]> getAvatar(HttpSession session) {
        long userId = (long) session.getAttribute("userId");
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(userService.getUserAvatar(userId));
    }

    @PostMapping
    public ResponseEntity<String> updateAvatar(HttpSession session, @RequestParam("avatar") byte[] newAvatar) {
        long userId = (long) session.getAttribute("userId");
        userService.updateUserAvatar(userId, newAvatar);
        return ResponseEntity.ok("Avatar updated successfully");
    }
}
