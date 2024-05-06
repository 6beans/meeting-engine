package ru.sixbeans.meetingengine.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import ru.sixbeans.meetingengine.entity.Event;
import ru.sixbeans.meetingengine.entity.PersonalInfo;
import ru.sixbeans.meetingengine.entity.Tag;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.model.PersonalInfoData;
import ru.sixbeans.meetingengine.model.UserData;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Named("eventToEventId")
    static Collection<Long> eventToEventId(Collection<Event> events) {
        return events.stream()
                .map(Event::getId)
                .toList();
    }

    @Named("tagToTagId")
    static Collection<Long> tagToTagId(Collection<Tag> tags) {
        return tags.stream()
                .map(Tag::getId)
                .toList();
    }

    @Mapping(source = "tags", target = "tagIds", qualifiedByName = "tagToTagId")
    @Mapping(source = "events", target = "eventIds", qualifiedByName = "eventToEventId")
    @Mapping(source = "memberEvents", target = "memberEventIds", qualifiedByName = "eventToEventId")
    UserData map(User user);

    List<UserData> map(Collection<User> users);

    PersonalInfoData map(PersonalInfo personalInfo);
}
