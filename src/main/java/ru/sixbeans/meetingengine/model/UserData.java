package ru.sixbeans.meetingengine.model;

import ru.sixbeans.meetingengine.entity.User;

import java.util.Base64;

public record UserData(
        String email,
        String fullName,
        String userName,
        String avatar
) {

    public static UserData from(User user) {
        return new UserData(
                user.getEmail(),
                user.getFullName(),
                user.getUserName(),
                "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(user.getAvatar())
        );
    }
}
