package ru.sixbeans.meetingengine.service.impl.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sixbeans.meetingengine.entity.DeletedEvent;
import ru.sixbeans.meetingengine.entity.Event;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.exception.EventNotFoundException;
import ru.sixbeans.meetingengine.repository.DeletedEventRepository;
import ru.sixbeans.meetingengine.repository.EventRepository;
import ru.sixbeans.meetingengine.repository.UserRepository;
import ru.sixbeans.meetingengine.service.impl.user.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class EventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final DeletedEventRepository deletedEventRepository;

    public EventService(EventRepository eventRepository, UserRepository userRepository, UserService userService, DeletedEventRepository deletedEventRepository) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.deletedEventRepository = deletedEventRepository;
    }


    public Event findById(long id) throws EventNotFoundException {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event with id = " + id + " not found"));
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Set<Event> findByOwnerName(String name) {
        User owner = userService.findByUsername(name);
        return owner.getOwned();
    }

    public Set<Event> findByOwnerToken(OAuth2AuthenticationToken token) {
        User owner = userService.findByToken(token);
        return owner.getOwned();
    }

    public Set<Event> findByMemberName(String name) {
        User owner = userService.findByUsername(name);
        return owner.getParticipated();
    }

    public Set<Event> findByMemberToken(OAuth2AuthenticationToken token) {
        User owner = userService.findByToken(token);
        return owner.getParticipated();
    }


    @Transactional
    public void save(Event event, OAuth2AuthenticationToken token) {
        User owner = userService.findByToken(token);
        owner.getOwned().add(event);
        event.setCreateDate(LocalDate.now());
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

        eventRepository.save(updatedEvent);
    }

    @Transactional
    public void delete(long id) {
        eventRepository.deleteById(id);
    }

    @Transactional
    public void joinToEvent(OAuth2AuthenticationToken token, long eventId) {
        User user = userService.findByToken(token);
        Event event = findById(eventId);

        event.getMembers().add(user);
    }

    @Transactional
    public void leaveFromEvent(OAuth2AuthenticationToken token, long eventId) {
        User user = userService.findByToken(token);
        Event event = findById(eventId);

        event.getMembers().remove(user);
    }

    @Transactional
    public void deleteAllByEndDateAfter(LocalDate localDate) {
        findAll().stream()
                .filter((event -> event.getEndDate().isBefore(localDate)))
                .forEach(event -> {
                    deletedEventRepository.save(new DeletedEvent(event));
                    delete(event.getId());
                });

    }

    @Transactional
    public void addMember(long eventId, long userId) throws EventNotFoundException, UsernameNotFoundException {
        Event event = findById(eventId);
        User user = userService.findByUserId(userId);
        event.getMembers().add(user);
    }

    @Transactional
    public void deleteMember(long eventId, long userID) throws EventNotFoundException, UsernameNotFoundException {
        Event event = findById(eventId);
        User user = userService.findByUserId(userID);

        event.getMembers().remove(user);

    }
}
