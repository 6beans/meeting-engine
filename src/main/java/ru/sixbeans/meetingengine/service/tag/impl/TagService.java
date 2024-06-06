package ru.sixbeans.meetingengine.service.tag.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sixbeans.meetingengine.entity.Tag;
import ru.sixbeans.meetingengine.repository.EventRepository;
import ru.sixbeans.meetingengine.repository.TagRepository;
import ru.sixbeans.meetingengine.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    private Tag getOrCreate(String id) {
        return tagRepository.findById(id)
                .orElseGet(() -> tagRepository.save(
                        Tag.builder().id(id).build()
                ));
    }

    @Transactional(readOnly = true)
    public Collection<String> findAllUserTags(String userId) {
        return userRepository.getReferenceById(userId).getTags()
                .stream().map(Tag::getId).toList();
    }

    @Transactional
    public void updateUserTags(String userId, Collection<String> tags) {
        userRepository.getReferenceById(userId).setTags(
                tags.stream().map(this::getOrCreate)
                        .collect(Collectors.toSet())
        );
    }

    @Transactional(readOnly = true)
    public Collection<String> findAllEventTags(long eventId) {
        return eventRepository.getReferenceById(eventId).getTags()
                .stream().map(Tag::getId).toList();
    }

    @Transactional
    public void updateEventTags(long eventId, Collection<String> tags) {
        eventRepository.getReferenceById(eventId).setTags(
                tags.stream().map(this::getOrCreate)
                        .collect(Collectors.toSet())
        );
    }
}
