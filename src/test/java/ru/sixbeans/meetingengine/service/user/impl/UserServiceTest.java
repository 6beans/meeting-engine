package ru.sixbeans.meetingengine.service.user.impl;

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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private EntityManager entityManager;

    private User user1, user2;

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
    }

    @Test
    void getUserByUserName() {
        UserData result = userService.getUser("@username1");
        assertThat(result.userName()).isEqualTo("@username1");
    }

    @Test
    void getUserByUserId() {
        UserData result = userService.getUser(user1.getId());
        assertThat(result.userName()).isEqualTo("@username1");
    }

    @Test
    void getUsers() {
        Collection<UserData> result = userService.getUsers(Arrays.asList(user1.getId(), user2.getId()));
        assertThat(result).hasSize(2);
    }

    @Test
    void getUserNotFound() {
        assertThatThrownBy(() -> userService.getUser(999L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void updateUserAvatar() {
        byte[] newAvatar = new byte[]{1, 2, 3};
        userService.updateUserAvatar(user1.getId(), newAvatar);
        entityManager.flush();
        User updatedUser = entityManager.find(User.class, user1.getId());
        assertThat(updatedUser.getAvatar()).isEqualTo(newAvatar);
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

    @Test
    void findMutualSubscriptions() {
        userService.subscribe(user1.getId(), user2.getId());
        userService.subscribe(user2.getId(), user1.getId());
        entityManager.flush();

        List<UserData> mutual = userService.findMutualSubscriptions(user1.getId());
        assertThat(mutual).hasSize(1);
    }
}
