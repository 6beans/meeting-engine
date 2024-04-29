package ru.sixbeans.meetingengine.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.sixbeans.meetingengine.repository.TagRepository;
import ru.sixbeans.meetingengine.repository.UserRepository;
import ru.sixbeans.meetingengine.service.impl.UserService;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(UserService.class)
public class UserTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TagRepository tagRepository;

    @Test
    void testAddFriend() {
        User user1 = new User();
        user1.setFullName("user1");
        user1.setUserName("user1");
        user1.setExternalId("11111111111");
        user1.setEmail("user1@example.com");
        user1.setProfileCompleted(false);

        User user2 = new User();
        user2.setFullName("user2");
        user2.setUserName("user2");
        user2.setExternalId("222222222222");
        user2.setEmail("user2@example.com");
        user2.setProfileCompleted(false);

        userService.addFriend(user1, user2);

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        User foundUser1 = userRepository.findById(user1.getId()).orElseThrow();
        User foundUser2 = userRepository.findById(user2.getId()).orElseThrow();

        assertThat(foundUser1.getFriends()).contains(user2);
        assertThat(foundUser2.getFriends()).contains(user1);
    }

    @Test
    void testRemoveFriend() {
        User user1 = new User();
        user1.setFullName("user1");
        user1.setUserName("user1");
        user1.setExternalId("11111111111");
        user1.setEmail("user1@example.com");
        user1.setProfileCompleted(false);

        User user2 = new User();
        user2.setFullName("user2");
        user2.setUserName("user2");
        user2.setExternalId("222222222222");
        user2.setEmail("user2@example.com");
        user2.setProfileCompleted(false);

        userService.addFriend(user1, user2);

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        userService.removeFriend(user1, user2);

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        User foundUser1 = userRepository.findById(user1.getId()).orElseThrow();
        User foundUser2 = userRepository.findById(user2.getId()).orElseThrow();

        assertThat(foundUser1.getFriends()).doesNotContain(user2);
        assertThat(foundUser2.getFriends()).doesNotContain(user1);
    }

    @Test
    void testAddTagToUser() {
        User user = new User();
        user.setFullName("user");
        user.setExternalId("0000000000");
        user.setUserName("userWithTag");
        user.setEmail("userWithTag@example.com");
        user.setProfileCompleted(false);

        Tag tag = new Tag();
        tag.setTitle("SpringBoot");
        tag.setCategory("Framework");

        userService.addTag(user, tag);

        entityManager.persist(user);
        entityManager.persist(tag);
        entityManager.flush();

        User foundUser = userRepository.findById(user.getId()).orElseThrow();
        Tag foundTag = tagRepository.findById(tag.getId()).orElseThrow();

        assertThat(foundUser.getTags()).contains(tag);
        assertThat(foundTag.getUsers()).contains(user);
    }

    @Test
    void testRemoveTagFromUser() {
        User user = new User();
        user.setFullName("user");
        user.setExternalId("0000000000");
        user.setUserName("userWithoutTag");
        user.setEmail("userWithoutTag@example.com");
        user.setProfileCompleted(false);

        Tag tag = new Tag();
        tag.setTitle("Java");
        tag.setCategory("Programming Language");

        userService.addTag(user, tag);

        entityManager.persist(user);
        entityManager.persist(tag);
        entityManager.flush();

        userService.removeTag(user, tag);

        entityManager.persist(user);
        entityManager.flush();

        User foundUser = userRepository.findById(user.getId()).orElseThrow();
        Tag foundTag = tagRepository.findById(tag.getId()).orElseThrow();

        assertThat(foundUser.getTags()).doesNotContain(tag);
        assertThat(foundTag.getUsers()).doesNotContain(user);
    }
}
