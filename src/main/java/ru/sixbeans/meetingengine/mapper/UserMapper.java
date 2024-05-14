package ru.sixbeans.meetingengine.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import ru.sixbeans.meetingengine.entity.Event;
import ru.sixbeans.meetingengine.entity.PersonalInfo;
import ru.sixbeans.meetingengine.entity.Tag;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.model.PersonalInfoData;
import ru.sixbeans.meetingengine.model.TagData;
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

    @Named("tagToTagData")
    static Collection<TagData> tagToTagData(Collection<Tag> tags) {
        return tags.stream()
                .map(tag -> new TagData(tag.getId(), tag.getTitle(), tag.getCategory()))
                .toList();
    }

    UserData map(User user);

    List<UserData> map(Collection<User> users);

    PersonalInfoData map(PersonalInfo personalInfo);
}
