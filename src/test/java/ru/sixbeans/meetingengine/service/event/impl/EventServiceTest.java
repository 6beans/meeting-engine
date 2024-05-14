package ru.sixbeans.meetingengine.service.event.impl;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.sixbeans.meetingengine.entity.Event;
import ru.sixbeans.meetingengine.entity.PersonalInfo;
import ru.sixbeans.meetingengine.entity.User;

import java.time.LocalDate;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private EntityManager entityManager;

    private User user;
    private Event event;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .issuer("issuer")
                .subject("externalId1")
                .userName("@username1")
                .email("email1@example.com")
                .personalInfo(PersonalInfo.builder()
                        .fullName("Full Name")
                        .build())
                .build();

        event = Event.builder().id(null).endDate(LocalDate.now().plusDays(1))
                .title("Test Event").isActive(true).members(new HashSet<>()).build();
        event.getMembers().add(user);

        entityManager.persist(user);
        entityManager.persist(event);
        entityManager.flush();
    }

    @Test
    void testAddMemberToEvent() {
        eventService.addMemberToEvent(event.getId(), user.getId());
        entityManager.flush();

        Event updatedEvent = entityManager.find(Event.class, event.getId());
        assertThat(updatedEvent.getMembers()).contains(user);
    }

    @Test
    void testRemoveMemberFromEvent() {
        eventService.removeMemberFromEvent(event.getId(), user.getId());
        entityManager.flush();

        Event updatedEvent = entityManager.find(Event.class, event.getId());
        assertThat(updatedEvent.getMembers()).doesNotContain(user);
    }

    @Test
    void testUpdateEventsActivity() {
        eventService.updateEventsActivity(LocalDate.now());
        entityManager.flush();

        Event updatedEvent = entityManager.find(Event.class, event.getId());
        assertThat(updatedEvent.getIsActive()).isFalse();
    }
}
