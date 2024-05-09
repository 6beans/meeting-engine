package ru.sixbeans.meetingengine.mapper;

import org.mapstruct.*;
import ru.sixbeans.meetingengine.entity.Event;
import ru.sixbeans.meetingengine.entity.Tag;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.model.EventData;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    @Named("userToUserId")
    static Long userToUserId(User user) {
        return user.getId();
    }

    @Named("tagToTagId")
    static Collection<Long> tagToTagId(Collection<Tag> tags) {
        return tags.stream()
                .map(Tag::getId)
                .toList();
    }

    @Mapping(source = "owner", target = "ownerId", qualifiedByName = "userToUserId")
    @Mapping(source = "members", target = "memberIds", qualifiedByName = "userToUserId")
    @Mapping(source = "tags", target = "tagIds", qualifiedByName = "tagToTagId")
    EventData map(Event event);

    Event map(EventData eventData);

    List<EventData> map(List<Event> events);

    void map(EventData eventData, @MappingTarget Event event);
}
