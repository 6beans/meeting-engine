package ru.sixbeans.meetingengine.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.exception.UserNotFoundException;
import ru.sixbeans.meetingengine.mapper.UserMapper;
import ru.sixbeans.meetingengine.model.PersonalInfoData;
import ru.sixbeans.meetingengine.model.UserData;
import ru.sixbeans.meetingengine.repository.UserRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper mapper;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserData getUserByUsername(String userName) {
        var user = userRepository.findByUserName(userName);
        if (user.isEmpty() || !userName.startsWith("@"))
            throw new UserNotFoundException(userName);
        else return mapper.map(user.get());
    }

    @Transactional(readOnly = true)
    public UserData getUserById(long userId) {
        return mapper.map(userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId)));
    }

    @Transactional(readOnly = true)
    public PersonalInfoData getUserPersonalInfoById(long userId) {
        return mapper.map(userRepository.findById(userId).map(User::getPersonalInfo)
                .orElseThrow(() -> new UserNotFoundException(userId)));
    }

    @Transactional(readOnly = true)
    public List<UserData> findAllUserSubscriptions(long userId) {
        return mapper.map(userRepository.getReferenceById(userId).getSubscriptions());
    }

    @Transactional(readOnly = true)
    public List<UserData> findAllUserSubscribers(long userId) {
        return mapper.map(userRepository.getReferenceById(userId).getSubscribers());
    }

    @Transactional(readOnly = true)
    public boolean isSubscriber(long userId, long subscriberId) {
        var subscribers = userRepository.getReferenceById(userId).getSubscribers();
        var subscriber = userRepository.getReferenceById(subscriberId);
        return subscribers.contains(subscriber);
    }

    @Transactional(readOnly = true)
    public boolean isSubscription(long userId, long subscriptionId) {
        var subscriptions = userRepository.getReferenceById(userId).getSubscriptions();
        var subscription = userRepository.getReferenceById(subscriptionId);
        return subscriptions.contains(subscription);
    }

    @Transactional
    public void subscribe(long userId, long subscriptionId) {
        User user = userRepository.getReferenceById(userId);
        User subscription = userRepository.getReferenceById(subscriptionId);
        user.getSubscriptions().add(subscription);
        subscription.getSubscribers().add(user);
    }

    @Transactional
    public void unsubscribe(long userId, long subscriptionId) {
        User user = userRepository.getReferenceById(userId);
        User subscription = userRepository.getReferenceById(subscriptionId);
        user.getSubscriptions().remove(subscription);
        subscription.getSubscribers().remove(user);
    }

    @Transactional(readOnly = true)
    public List<UserData> findMutualSubscriptions(long userId) {
        User user = userRepository.getReferenceById(userId);
        Set<User> subscriptions = user.getSubscriptions();
        Set<User> subscribers = user.getSubscribers();
        return subscriptions.stream()
                .filter(subscribers::contains)
                .map(mapper::map)
                .toList();
    }
}
