package ru.sixbeans.meetingengine.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.sixbeans.meetingengine.entity.PersonalInfo;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.mapper.UserMapperImpl;
import ru.sixbeans.meetingengine.model.UserData;
import ru.sixbeans.meetingengine.service.user.impl.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({UserService.class, UserMapperImpl.class})
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private TestEntityManager entityManager;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .subject("externalId1")
                .provider("provider")
                .userName("@username1")
                .email("email1@example.com")
                .personalInfo(PersonalInfo.builder()
                        .fullName("Full Name")
                        .build())
                .build();

        user2 = User.builder()
                .subject("externalId2")
                .provider("provider")
                .userName("@username2")
                .email("email2@example.com")
                .personalInfo(PersonalInfo.builder()
                        .fullName("Full Name")
                        .build())
                .build();

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        entityManager.persist(user1);
        entityManager.flush();
    }

    @Test
    void testGetUserByUsername() {
        UserData userData = userService.getUserByUsername(user1.getUserName());
        assertThat(userData).isNotNull();
        assertThat(userData.userName()).isEqualTo(user1.getUserName());
    }

    @Test
    void testGetUserById() {
        UserData userData = userService.getUserById(user1.getId());
        assertThat(userData).isNotNull();
        assertThat(userData.userName()).isEqualTo(user1.getUserName());
    }

    @Test
    void testSubscribeAndUnsubscribe() {
        userService.subscribe(user1.getId(), user2.getId());
        assertThat(userService.isSubscription(user1.getId(), user2.getId())).isTrue();

        userService.unsubscribe(user1.getId(), user2.getId());
        assertThat(userService.isSubscription(user1.getId(), user2.getId())).isFalse();
    }

    @Test
    void testFindMutualSubscriptions() {
        userService.subscribe(user1.getId(), user2.getId());
        userService.subscribe(user2.getId(), user1.getId());

        List<UserData> mutualSubscriptionsUser1 = userService.findMutualSubscriptions(user1.getId());
        List<UserData> mutualSubscriptionsUser2 = userService.findMutualSubscriptions(user2.getId());

        assertThat(mutualSubscriptionsUser1).isNotEmpty();
        assertThat(mutualSubscriptionsUser1).hasSize(1);
        assertThat(mutualSubscriptionsUser1.getFirst().userName())
                .isEqualTo(user2.getUserName());

        assertThat(mutualSubscriptionsUser2).isNotEmpty();
        assertThat(mutualSubscriptionsUser2).hasSize(1);
        assertThat(mutualSubscriptionsUser2.getFirst()
                .userName()).isEqualTo(user1.getUserName());
    }
}
