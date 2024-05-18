package ru.sixbeans.meetingengine.service.chanceMeeting.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.exception.UserNotFoundException;
import ru.sixbeans.meetingengine.mapper.UserMapper;
import ru.sixbeans.meetingengine.model.PersonalInfoData;
import ru.sixbeans.meetingengine.model.UserData;
import ru.sixbeans.meetingengine.repository.PersonalInfoRepository;
import ru.sixbeans.meetingengine.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChanceMeetingService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PersonalInfoRepository personalInfoRepository;

    public ChanceMeetingStatus getChanceMeetingStatus(long userId) {
        return userRepository.getReferenceById(userId).getChanceMeetingStatus();
    }

    public UserData getUserPartner(long userId) {
        return userMapper.map(userRepository.getPartnerFromUserMeetings(userId)
                .orElseThrow(()-> new UserNotFoundException("Couldn't find partner with id: " + userId)));
    }

    public PersonalInfoData getUserPartnerPersonalInfo(long userId) {
        return userMapper.map(personalInfoRepository.getPartnerPersonalInfoFromUserMeetings(userId)
                .orElseThrow(()-> new UserNotFoundException("Couldn't find partner personal info a user with id: " + userId)));
    }

    @Transactional
    public void changeUserChanceMeetingStatus(long userId, ChanceMeetingStatus newStatus) {
        userRepository.updateChanceMeetingStatusByUserId(userId, newStatus);
    }

    @Transactional
    public void changeUsersChanceMeetingStatus(ChanceMeetingStatus oldStatus, ChanceMeetingStatus newStatus) {
        userRepository.updateChanceMeetingStatusByOldStatus(oldStatus, newStatus);
    }

    @Transactional
    public void createChanceMeetingPairsOfUsers() {
        final int DEFAULT_PAGE_SIZE = 1000;
        List<User> users;
        Random random = new Random();

        PageRequest pageRequest = PageRequest.of(0, DEFAULT_PAGE_SIZE);
        for (int i = 0;
             !(users = userRepository.findAllByChanceMeetingStatus(pageRequest, ChanceMeetingStatus.ENROLLED)).isEmpty();
             i++, pageRequest = PageRequest.of(i, DEFAULT_PAGE_SIZE)) {

            // Check if we have an extra user for whom we can't find a pair
            if (users.size() % 2 != 0) {
                User lastUser = users.removeLast();
                User randomUser = users.get(random.nextInt(users.size()));

                lastUser.getUserMeetings().add(randomUser);
            }

            Collections.shuffle(users);

            // Divide by pairs
            for (int j = 0; j < users.size() - 1; j+=2) {
                User user1 = users.get(j);
                User user2 = users.get(j+1);

                user1.getUserMeetings().add(user2);
                user2.getUserMeetings().add(user1);
            }
        }
    }
}
