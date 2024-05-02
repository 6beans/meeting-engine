package ru.sixbeans.meetingengine.model;

import lombok.Data;
import org.modelmapper.ModelMapper;
import ru.sixbeans.meetingengine.entity.Event;
import ru.sixbeans.meetingengine.entity.Tag;
import ru.sixbeans.meetingengine.entity.User;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public record EventData(
        String name,
        String description,
        Long id,
        Set<Tag> tags,
        UserData owner,
        Set<UserData> members,
        LocalDate createDate,
        LocalDate endDate
) {
    public static EventData from(Event event) {
        return new EventData(
                event.getName(),
                event.getDescription(),
                event.getId(),
                event.getTags(),
                UserData.from(event.getOwner()),
                event.getMembers().stream().map(UserData::from).collect(Collectors.toSet()),
                event.getCreateDate(),
                event.getEndDate()
        );
    }
}