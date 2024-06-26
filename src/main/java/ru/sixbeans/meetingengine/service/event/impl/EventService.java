package ru.sixbeans.meetingengine.service.event.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sixbeans.meetingengine.entity.Event;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.exception.EventNotFoundException;
import ru.sixbeans.meetingengine.mapper.EventMapper;
import ru.sixbeans.meetingengine.model.EventData;
import ru.sixbeans.meetingengine.repository.EventRepository;
import ru.sixbeans.meetingengine.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventMapper mapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public Collection<String> findAllEventMemberIds(long eventId) {
        return eventRepository.findById(eventId).stream()
                .map(Event::getMembers).flatMap(Collection::stream)
                .map(User::getId).toList();
    }

    public Collection<EventData> findAllEventsByOwnerId(String ownerId) {
        return eventRepository.findAllByOwnerId(ownerId).stream()
                .map(mapper::map).toList();
    }

    public Page<EventData> findAllEventsByOwnerId(String ownerId, Pageable pageable) {
        return eventRepository.findAllByOwnerId(ownerId, pageable).map(mapper::map);
    }

    public EventData getById(long eventId) {
        return eventRepository.findById(eventId).map(mapper::map)
                .orElseThrow(() -> new EventNotFoundException(eventId));
    }

    public List<EventData> findAllActive() {
        return mapper.map(eventRepository.findAllByIsActive(true));
    }

    public List<EventData> findAllNonActive() {
        return mapper.map(eventRepository.findAllByIsActive(false));
    }

    public List<EventData> findAllByOwnerId(String ownerId) {
        return mapper.map(eventRepository.findAllByOwnerId(ownerId));
    }

    @Transactional
    public void addEventToUser(EventData eventData, String userId) {
        User user = userRepository.getReferenceById(userId);
        Event event = mapper.map(eventData);
        event.setOwner(user);
        eventRepository.save(event);
    }

    @Transactional
    public EventData updateEvent(Long eventId, EventData eventData) {
        Event event = eventRepository.getReferenceById(eventId);
        mapper.map(eventData, event);
        return mapper.map(eventRepository.save(event));
    }

    @Transactional
    public void addMemberToEvent(String userId, long eventId) {
        var user = userRepository.getReferenceById(userId);
        var event = eventRepository.getReferenceById(eventId);
        event.getMembers().add(user);
    }

    @Transactional
    public void removeMemberFromEvent(String userId, long eventId) {
        var user = userRepository.getReferenceById(userId);
        var event = eventRepository.getReferenceById(eventId);
        event.getMembers().remove(user);
    }

    @Transactional
    public void updateEventsActivity(LocalDateTime expirationDate) {
        List<Event> activeEvents = eventRepository.findAllByIsActive(true);
        activeEvents.stream()
                .filter(event -> expirationDate.isBefore(event.getEndDate()))
                .forEach(event -> event.setIsActive(false));
    }

    @Transactional(readOnly = true)
    public byte[] getEventPicture(long eventId) {
        return eventRepository.getReferenceById(eventId)
                .getPicture();
    }

    @Transactional
    public void updateEventPicture(long eventId, byte[] avatar) {
        eventRepository.getReferenceById(eventId)
                .setPicture(avatar);
    }
}
