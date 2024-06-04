package ru.sixbeans.meetingengine.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.exception.UserNotFoundException;
import ru.sixbeans.meetingengine.mapper.UserMapper;
import ru.sixbeans.meetingengine.model.UserData;
import ru.sixbeans.meetingengine.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper mapper;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserData getUser(long userId) {
        return userRepository.findById(userId).map(mapper::map)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Transactional(readOnly = true)
    public Collection<UserData> getUsers(Collection<Long> userIds) {
        return userIds.stream().map(userRepository::findById)
                .flatMap(Optional::stream).map(mapper::map)
                .toList();
    }

    @Transactional(readOnly = true)
    public byte[] getUserAvatar(long userId) {
        return userRepository.getReferenceById(userId)
                .getAvatar();
    }

    @Transactional
    public void updateUserAvatar(long userId, byte[] avatar) {
        userRepository.getReferenceById(userId)
                .setAvatar(avatar);
    }

    @Transactional(readOnly = true)
    public List<UserData> findAllUserSubscriptions(long userId) {
        return userRepository.findById(userId).map(User::getSubscriptions)
                .map(mapper::map).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Transactional(readOnly = true)
    public List<UserData> findAllUserSubscribers(long userId) {
        return userRepository.findById(userId).map(User::getSubscribers)
                .map(mapper::map).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Transactional(readOnly = true)
    public boolean isSubscriber(long userId, long subscriberId) {
        return userRepository.existsByIdAndSubscribers_Id(userId, subscriberId);
    }

    @Transactional(readOnly = true)
    public boolean isSubscription(long userId, long subscriptionId) {
        return userRepository.existsByIdAndSubscriptions_Id(userId, subscriptionId);
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
