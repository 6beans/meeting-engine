package ru.sixbeans.meetingengine.mapper;

import org.mapstruct.*;
import ru.sixbeans.meetingengine.entity.Event;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.model.EventData;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    @Named("userToUserId")
    static String userToUserId(User user) {
        return user.getId();
    }

    @Mapping(source = "owner", target = "ownerId", qualifiedByName = "userToUserId")
    EventData map(Event event);

    Event map(EventData eventData);

    List<EventData> map(List<Event> events);

    void map(EventData eventData, @MappingTarget Event event);
}
