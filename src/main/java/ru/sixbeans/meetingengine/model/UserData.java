package ru.sixbeans.meetingengine.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.sixbeans.meetingengine.entity.Event;
import ru.sixbeans.meetingengine.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public record UserData(
        Long id,
        String email,
        String fullName,
        String userName,
        byte[] avatar,
        String profileDescription,
        String tgLink,
        String vkLink,
        Set<TagData> tags
){
    public static UserData from(User user) {
        return new UserData(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getUserName(),
                user.getAvatar(),
                user.getProfileDescription(),
                user.getUserContacts().getTelegramLink(),
                user.getUserContacts().getVkLink(),
                user.getTags().stream().map(TagData::from).collect(Collectors.toSet())
        );
    }
}