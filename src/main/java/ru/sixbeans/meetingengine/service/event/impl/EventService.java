package ru.sixbeans.meetingengine.service.event.impl;

import lombok.RequiredArgsConstructor;
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
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventMapper mapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public Collection<Long> findAllEventMemberIds(long eventId) {
        return eventRepository.findById(eventId).stream()
                .map(Event::getMembers).flatMap(Collection::stream)
                .map(User::getId).toList();
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

    public List<EventData> findAllByOwnerId(long ownerId) {
        return mapper.map(eventRepository.findAllByOwnerId(ownerId));
    }

    @Transactional
    public void addEventToUser(EventData eventData, long userId) {
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
    public void joinEvent(long userId, long eventId) {
        var user = userRepository.getReferenceById(userId);
        var event = eventRepository.getReferenceById(eventId);
        event.getMembers().add(user);
    }

    @Transactional
    public void leaveEvent(long userId, long eventId) {
        var user = userRepository.getReferenceById(userId);
        var event = eventRepository.getReferenceById(eventId);
        event.getMembers().remove(user);
    }

    @Transactional
    public void updateEventsActivity(LocalDate expirationDate) {
        List<Event> activeEvents = eventRepository.findAllByIsActive(true);
        activeEvents.stream()
                .filter(event -> expirationDate.isBefore(event.getEndDate()))
                .forEach(event -> event.setIsActive(false));
    }
}
