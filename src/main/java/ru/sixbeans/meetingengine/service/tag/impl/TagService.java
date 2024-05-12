package ru.sixbeans.meetingengine.service.tag.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sixbeans.meetingengine.entity.Event;
import ru.sixbeans.meetingengine.entity.Tag;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.exception.EventNotFoundException;
import ru.sixbeans.meetingengine.exception.TagNotFoundException;
import ru.sixbeans.meetingengine.exception.UserNotFoundException;
import ru.sixbeans.meetingengine.mapper.TagMapper;
import ru.sixbeans.meetingengine.model.TagData;
import ru.sixbeans.meetingengine.repository.EventRepository;
import ru.sixbeans.meetingengine.repository.TagRepository;
import ru.sixbeans.meetingengine.repository.UserRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TagMapper tagMapper;

    @Transactional(readOnly = true)
    public Collection<TagData> findAllUserTags(long userId) {
        return userRepository.findById(userId)
                .map(User::getTags).map(tagMapper::map)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Transactional(readOnly = true)
    public Collection<TagData> findAllEventTags(long eventId) {
        return eventRepository.findById(eventId)
                .map(Event::getTags).map(tagMapper::map)
                .orElseThrow(() -> new EventNotFoundException(eventId));
    }

    public TagData getTag(long tagId) {
        return tagRepository.findById(tagId).map(tagMapper::map)
                .orElseThrow(() -> new TagNotFoundException(tagId));
    }

    public Collection<TagData> getTags(Collection<Long> tagIds) {
        return tagIds.stream().map(tagRepository::findById)
                .flatMap(Optional::stream).map(tagMapper::map)
                .toList();
    }

    @Transactional
    public void addTagToUser(long userId, long tagId) {
        User user = userRepository.getReferenceById(userId);
        Tag tag = tagRepository.getReferenceById(tagId);
        user.getTags().add(tag);
    }

    @Transactional
    public void removeTagFromUser(long userId, long tagId) {
        User user = userRepository.getReferenceById(userId);
        Tag tag = tagRepository.getReferenceById(tagId);
        user.getTags().remove(tag);
    }

    @Transactional
    public void addTagToEvent(long eventId, long tagId) {
        Event event = eventRepository.getReferenceById(eventId);
        Tag tag = tagRepository.getReferenceById(tagId);
        event.getTags().add(tag);
    }

    @Transactional
    public void removeTagFromEvent(long eventId, long tagId) {
        Event event = eventRepository.getReferenceById(eventId);
        Tag tag = tagRepository.getReferenceById(tagId);
        event.getTags().remove(tag);
    }
}
