package ru.sixbeans.meetingengine.service.chanceMeeting.impl;

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
import ru.sixbeans.meetingengine.model.PersonalInfoData;
import ru.sixbeans.meetingengine.model.UserData;
import ru.sixbeans.meetingengine.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ChanceMeetingServiceTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ChanceMeetingService chanceMeetingService;

    @Autowired
    private UserRepository userRepository;

    private User user1, user2;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .issuer("issuer")
                .subject("externalId1")
                .userName("@username1")
                .email("email1@example.com")
                .build();

        user2 = User.builder()
                .issuer("issuer")
                .subject("externalId2")
                .userName("@username2")
                .email("email2@example.com")
                .build();

        user1.setPersonalInfo(PersonalInfo.builder()
                .fullName("User1 Full Name")
                .user(user1)
                .build());
        user2.setPersonalInfo(PersonalInfo.builder()
                .fullName("User2 Full Name")
                .user(user2)
                .build());

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();
    }

    @Test
    void getChanceMeetingStatusTest() {
        ChanceMeetingStatus chanceMeetingStatus = chanceMeetingService.getChanceMeetingStatus(user1.getId());
        assertThat(chanceMeetingStatus).isEqualTo(ChanceMeetingStatus.NOT_REGISTERED);

        user2.setChanceMeetingStatus(ChanceMeetingStatus.ENROLLED);
        entityManager.persist(user2);
        entityManager.flush();

        chanceMeetingStatus = chanceMeetingService.getChanceMeetingStatus(user2.getId());
        assertThat(chanceMeetingStatus).isEqualTo(ChanceMeetingStatus.ENROLLED);

        user1.setChanceMeetingStatus(ChanceMeetingStatus.REGISTERED);
        entityManager.persist(user1);
        entityManager.flush();

        chanceMeetingStatus = chanceMeetingService.getChanceMeetingStatus(user1.getId());
        assertThat(chanceMeetingStatus).isEqualTo(ChanceMeetingStatus.REGISTERED);
    }

    @Test
    void getUserPartnerTest() {
        user1.getUserMeetings().add(user2);
        entityManager.persist(user1);
        entityManager.flush();

        UserData userData = chanceMeetingService.getUserPartner(user1.getId());
        assertThat(userData.userName()).isEqualTo(user2.getUserName());
    }

    @Test
    void getUserPartnerNotFoundTest() {
        assertThatThrownBy(() -> chanceMeetingService.getUserPartner(user1.getId()))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getUserPartnerPersonalInfoTest() {
        user1.getUserMeetings().add(user2);
        entityManager.persist(user1);
        entityManager.flush();

        PersonalInfoData personalInfoData = chanceMeetingService.getUserPartnerPersonalInfo(user1.getId());
        assertThat(personalInfoData.fullName()).isEqualTo(user2.getPersonalInfo().getFullName());
    }

    @Test
    void getUserPartnerPersonalInfoNotFoundTest() {
        assertThatThrownBy(() -> chanceMeetingService.getUserPartnerPersonalInfo(user1.getId()))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void changeUserChanceMeetingStatusTest() {
        chanceMeetingService.changeUserChanceMeetingStatus(user1.getId(), ChanceMeetingStatus.REGISTERED);
        entityManager.clear();

        ChanceMeetingStatus updatedStatus = chanceMeetingService.getChanceMeetingStatus(user1.getId());
        assertThat(updatedStatus).isEqualTo(ChanceMeetingStatus.REGISTERED);
    }

    @Test
    void createChanceMeetingPairsOfUsersTest() {
        user1.setChanceMeetingStatus(ChanceMeetingStatus.ENROLLED);

        user2.setChanceMeetingStatus(ChanceMeetingStatus.ENROLLED);

        User user3 = User.builder()
                .issuer("issuer")
                .subject("externalId3")
                .userName("@username3")
                .email("email3@example.com")
                .personalInfo(PersonalInfo.builder()
                        .fullName("Full Name 3")
                        .build())
                .chanceMeetingStatus(ChanceMeetingStatus.ENROLLED)
                .build();

        User user4 = User.builder()
                .issuer("issuer")
                .subject("externalId4")
                .userName("@username4")
                .email("email4@example.com")
                .personalInfo(PersonalInfo.builder()
                        .fullName("Full Name 4")
                        .build())
                .chanceMeetingStatus(ChanceMeetingStatus.ENROLLED)
                .build();

        entityManager.persist(this.user1);
        entityManager.persist(this.user2);
        entityManager.persist(user3);
        entityManager.persist(user4);
        entityManager.flush();

        // TODO: The test does not pass if you call the method through the service, but if you insert the method directly into the code and add entityManager.persist() in the for cycle, the test will pass
//        final int DEFAULT_PAGE_SIZE = 1000;
//        List<User> users;
//        Random random = new Random();
//
//        PageRequest pageRequest = PageRequest.of(0, DEFAULT_PAGE_SIZE);
//        for (int i = 0;
//             !(users = userRepository.findAllByChanceMeetingStatus(pageRequest, ChanceMeetingStatus.ENROLLED)).isEmpty();
//             i++, pageRequest = PageRequest.of(i, DEFAULT_PAGE_SIZE)) {
//
//            // Check if we have one excess user that we can divide by pairs
//            if (users.size() % 2 != 0) {
//                User lastUser = users.removeLast();
//                User randomUser = users.get(random.nextInt(users.size()));
//
//                lastUser.getUserMeetings().add(randomUser);
//                entityManager.persist(lastUser);
//                entityManager.flush();
//            }
//
//            Collections.shuffle(users);
//
//            // Divide by pairs
//            for (int j = 0; j < users.size() - 1; j+=2) {
//                User user1 = users.get(j);
//                User user2 = users.get(j+1);
//
//                user1.getUserMeetings().add(user2);
//                user2.getUserMeetings().add(user1);
//
//                entityManager.persist(user1);
//                entityManager.persist(user2);
//                entityManager.flush();
//            }
//        }

        chanceMeetingService.createChanceMeetingPairsOfUsers();

        entityManager.clear();

        User updatedUser1 = userRepository.getReferenceById(this.user1.getId());
        User updatedUser2 = userRepository.getReferenceById(this.user2.getId());
        User updatedUser3 = userRepository.getReferenceById(user3.getId());
        User updatedUser4 = userRepository.getReferenceById(user4.getId());

        System.out.println("User1 meetings: " + updatedUser1.getUserMeetings().size());
        System.out.println("User2 meetings: " + updatedUser2.getUserMeetings().size());
        System.out.println("User3 meetings: " + updatedUser3.getUserMeetings().size());
        System.out.println("User4 meetings: " + updatedUser4.getUserMeetings().size());

        assertThat(updatedUser1.getUserMeetings()).isNotEmpty();
        assertThat(updatedUser2.getUserMeetings()).isNotEmpty();
        assertThat(updatedUser3.getUserMeetings()).isNotEmpty();
        assertThat(updatedUser4.getUserMeetings()).isNotEmpty();
    }
}
