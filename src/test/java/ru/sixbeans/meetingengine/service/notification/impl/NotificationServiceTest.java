package ru.sixbeans.meetingengine.service.notification.impl;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.sixbeans.meetingengine.entity.PersonalInfo;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.exception.UserNotFoundException;
import ru.sixbeans.meetingengine.model.UserData;
import ru.sixbeans.meetingengine.service.user.impl.UserService;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class NotificationServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private EntityManager entityManager;

    private User user1, user2;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .issuer("issuer")
                .subject("externalId1")
                .userName("@username1")
                .email("meetis228@mail.ru")
                .personalInfo(PersonalInfo.builder()
                        .fullName("Full Name")
                        .build())
                .build();

        user2 = User.builder()
                .issuer("issuer")
                .subject("externalId2")
                .userName("@username2")
                .email("www.ion2005@mail.ru")
                .personalInfo(PersonalInfo.builder()
                        .fullName("Full Name")
                        .build())
                .build();

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();
    }

    @Test
    void subscribeAndUnsubscribe() {
        userService.subscribe(user1.getId(), user2.getId());
        entityManager.flush();
        assertThat(userService.isSubscriber(user2.getId(), user1.getId())).isTrue();

        userService.unsubscribe(user1.getId(), user2.getId());
        entityManager.flush();
        assertThat(userService.isSubscriber(user2.getId(), user1.getId())).isFalse();
    }

}
