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
import java.util.List;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventData getById(long eventId) {
        return eventMapper.map(eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId)));
    }

    public List<EventData> findAll() {
        return eventMapper.map(eventRepository.findAll());
    }

    public List<EventData> findAllActive() {
        return eventMapper.map(eventRepository.findAllByIsActive(true));
    }

    public List<EventData> findAllNonActive() {
        return eventMapper.map(eventRepository.findAllByIsActive(false));
    }

    public List<EventData> findAllByOwnerId(long ownerId) {
        return eventMapper.map(eventRepository.findAllByOwnerId(ownerId));
    }

    public List<EventData> findAllActiveByOwnerId(long ownerId) {
        return findAllByOwnerId(ownerId).stream()
                .filter(EventData::isActive)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EventData> findAllNonActiveByOwnerId(long ownerId) {
        return findAllByOwnerId(ownerId).stream()
                .filter(not(EventData::isActive))
                .toList();
    }

    @Transactional
    public void addEventToUser(EventData eventData, long userId) {
        User user = userRepository.getReferenceById(userId);
        Event event = eventMapper.map(eventData);
        user.getEvents().add(event);
    }

    @Transactional
    public EventData updateEvent(Long eventId, EventData eventData) {
        Event event = eventRepository.getReferenceById(eventId);
        eventMapper.map(eventData, event);
        return eventMapper.map(eventRepository.save(event));
    }

    @Transactional
    public void deleteById(long eventId) {
        eventRepository.deleteById(eventId);
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
        eventRepository.findAllByIsActive(true).stream()
                .filter(event -> expirationDate.isBefore(event.getEndDate()))
                .forEach(event -> event.setIsActive(false));
    }
}
