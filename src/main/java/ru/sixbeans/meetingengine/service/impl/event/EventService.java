package ru.sixbeans.meetingengine.service.impl.event;

import lombok.AllArgsConstructor;
import org.springframework.expression.AccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sixbeans.meetingengine.entity.Event;
import ru.sixbeans.meetingengine.entity.Tag;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.exception.EventInactiveException;
import ru.sixbeans.meetingengine.exception.EventNotFoundException;
import ru.sixbeans.meetingengine.repository.EventRepository;
import ru.sixbeans.meetingengine.repository.UserRepository;
import ru.sixbeans.meetingengine.service.impl.tag.TagService;
import ru.sixbeans.meetingengine.service.impl.user.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class EventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final TagService tagService;


    public Event findById(long id) throws EventNotFoundException {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event with id = " + id + " not found"));
    }

    public List<Event> findByIsActive(boolean isActive) {
        return eventRepository.findByIsActive(isActive);
    }

    public Event findByIdAndIsActive(long id, boolean isActive) throws EventNotFoundException {
        return eventRepository.findByIdAndIsActive(id, isActive)
                .orElseThrow(() -> new EventNotFoundException("Event with id = " + id + "and status " + (isActive ? "active" : "inactive") + " not found"));
    }


    public List<Event> findAll() {
        return eventRepository.findAll();
    }


    public Set<Event> findByOwnerId(long id) {
        return userService.findByUserId(id).getOwned();
    }

    public Set<Event> findByOwnerIdAndIsActive(long id, boolean isActive) {
        return findByOwnerId(id).stream().filter(event -> event.getIsActive() == isActive).collect(Collectors.toSet());
    }


    public Set<Event> findByOwnerToken(OAuth2AuthenticationToken token) {
        return userService.findByToken(token).getOwned();
    }

    public Set<Event> findByOwnerTokenAndIsActive(OAuth2AuthenticationToken token, boolean isActive) {
        return findByOwnerToken(token).stream().filter(event -> event.getIsActive() == isActive).collect(Collectors.toSet());
    }


    public Set<Event> findByMemberName(String name) {
        return userService.findByUsername(name).getParticipated();
    }

    public Set<Event> findByMemberNameAndIsActive(String name, boolean isActive) {
        return findByMemberName(name).stream().filter(event -> event.getIsActive() == isActive).collect(Collectors.toSet());
    }


    public Set<Event> findByMemberToken(OAuth2AuthenticationToken token) {
        return userService.findByToken(token).getParticipated();
    }

    public Set<Event> findByMemberTokenAndIsActive(OAuth2AuthenticationToken token, boolean isActive) {
        return findByMemberToken(token).stream().filter(event -> event.getIsActive() == isActive).collect(Collectors.toSet());
    }


    @Transactional
    public void save(Event event, OAuth2AuthenticationToken token) {
        User owner = userService.findByToken(token);
        owner.getOwned().add(event);
        event.setCreateDate(LocalDate.now());
        event.setIsActive(true);
        event.setOwner(owner);
        eventRepository.save(event);
    }

    @Transactional
    public void update(long oldEventId, Event updatedEvent) throws EventNotFoundException {
        Event oldEvent = findById(oldEventId);

        updatedEvent.setMembers(oldEvent.getMembers());
        updatedEvent.setOwner(oldEvent.getOwner());
        updatedEvent.setCreateDate(oldEvent.getCreateDate());
        updatedEvent.setId(oldEventId);
        updatedEvent.setIsActive(true);

        eventRepository.save(updatedEvent);
    }

    @Transactional
    public void delete(long id) {
        eventRepository.deleteById(id);
    }

    @Transactional
    public void joinToEvent(OAuth2AuthenticationToken token, long eventId) throws EventInactiveException {
        User user = userService.findByToken(token);
        Event event = findById(eventId);

        if (!event.getIsActive()) throw new EventInactiveException("Event inactive");

        event.getMembers().add(user);
    }

    @Transactional
    public void leaveFromEvent(OAuth2AuthenticationToken token, long eventId) throws EventInactiveException {
        User user = userService.findByToken(token);
        Event event = findById(eventId);

        if (!event.getIsActive()) throw new EventInactiveException("Event inactive");

        event.getMembers().remove(user);
    }

    @Transactional
    public void changeConditionAllEventsByEndDate(LocalDate localDate) {
        findAll().stream()
                .filter((event -> event.getEndDate().isBefore(localDate)))
                .forEach(event -> event.setIsActive(false));

    }

    @Transactional
    public void addMemberByUserName(long eventId, String userName) throws EventNotFoundException, UsernameNotFoundException {
        Event event = findById(eventId);
        User user = userService.findByUsername(userName);

        if (!event.getIsActive()) throw new EventInactiveException("Event inactive");

        event.getMembers().add(user);
    }

    @Transactional
    public void addMemberByUserId(long eventId, Long userId) throws EventNotFoundException, UsernameNotFoundException {
        Event event = findById(eventId);
        User user = userService.findByUserId(userId);

        if (!event.getIsActive()) throw new EventInactiveException("Event inactive");

        event.getMembers().add(user);
    }

    @Transactional
    public void deleteMemberByUserName(long eventId, String userName) throws EventNotFoundException, UsernameNotFoundException {
        Event event = findById(eventId);
        User user = userService.findByUsername(userName);

        if (!event.getIsActive()) throw new EventInactiveException("Event inactive");

        event.getMembers().remove(user);

    }

    @Transactional
    public void deleteMemberByUserId(long eventId, Long userId, OAuth2AuthenticationToken token) throws EventNotFoundException, UsernameNotFoundException, AccessException {
        Event event = findById(eventId);
        User user = userService.findByUserId(userId);
        User owner = userService.findByToken(token);

        if (event.getOwner() != owner) throw new AccessException("Only owner can delete members");
        if (!event.getIsActive()) throw new EventInactiveException("Event inactive");

        event.getMembers().remove(user);

    }

    @Transactional
    public void addTagByTagTitle(long eventId, String title) {
        Event event = findById(eventId);
        Tag tag = tagService.findByTitle(title);

        if (!event.getIsActive()) throw new EventInactiveException("Event inactive");

        event.getTags().add(tag);
    }

    @Transactional
    public void deleteTagByTagTitle(long eventId, String title) {
        Event event = findById(eventId);
        Tag tag = tagService.findByTitle(title);

        if (!event.getIsActive()) throw new EventInactiveException("Event inactive");

        event.getTags().remove(tag);
    }

}
