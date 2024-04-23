package ru.sixbeans.meetingengine.model;

import ru.sixbeans.meetingengine.entity.User;

public record UserData(
        String email,
        String fullName,
        String userName,
        byte[] avatar
) {

    public static UserData from(User user) {
        return new UserData(
                user.getEmail(),
                user.getFullName(),
                user.getUserName(),
                user.getAvatar()
        );
    }
}
