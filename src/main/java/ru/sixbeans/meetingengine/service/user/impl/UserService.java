package ru.sixbeans.meetingengine.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.exception.UserNotFoundException;
import ru.sixbeans.meetingengine.repository.UserRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public byte[] getUserAvatar(String subject) {
        return userRepository.getReferenceById(subject)
                .getAvatar();
    }

    @Transactional
    public void updateUserAvatar(String subject, byte[] avatar) {
        userRepository.getReferenceById(subject)
                .setAvatar(avatar);
    }

    @Transactional(readOnly = true)
    public List<String> findAllUserSubscriptions(String userId) {
        return userRepository.findById(userId).map(User::getSubscriptions)
                .orElseThrow(() -> new UserNotFoundException(userId))
                .stream().map(User::getId).toList();
    }

    @Transactional(readOnly = true)
    public List<String> findAllUserSubscribers(String userId) {
        return userRepository.getReferenceById(userId)
                .getSubscribers().stream()
                .map(User::getId).toList();
    }

    @Transactional(readOnly = true)
    public boolean isSubscriber(String userId, String subscriberId) {
        return userRepository.existsByIdAndSubscribers_Id(userId, subscriberId);
    }

    @Transactional(readOnly = true)
    public boolean isSubscription(String userId, String subscriptionId) {
        return userRepository.existsByIdAndSubscriptions_Id(userId, subscriptionId);
    }

    @Transactional
    public void subscribe(String userId, String subscriptionId) {
        User user = userRepository.getReferenceById(userId);
        User subscription = userRepository.getReferenceById(subscriptionId);
        user.getSubscriptions().add(subscription);
        subscription.getSubscribers().add(user);
    }

    @Transactional
    public void unsubscribe(String userId, String subscriptionId) {
        User user = userRepository.getReferenceById(userId);
        User subscription = userRepository.getReferenceById(subscriptionId);
        user.getSubscriptions().remove(subscription);
        subscription.getSubscribers().remove(user);
    }

    @Transactional(readOnly = true)
    public List<String> findMutualSubscriptions(String userId) {
        User user = userRepository.getReferenceById(userId);
        Set<User> subscriptions = user.getSubscriptions();
        Set<User> subscribers = user.getSubscribers();
        return subscriptions.stream()
                .filter(subscribers::contains)
                .map(User::getId)
                .toList();
    }
}
