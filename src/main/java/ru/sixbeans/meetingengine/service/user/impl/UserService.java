package ru.sixbeans.meetingengine.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.exception.UserNotFoundException;
import ru.sixbeans.meetingengine.mapper.UserMapper;
import ru.sixbeans.meetingengine.model.PersonalInfoData;
import ru.sixbeans.meetingengine.model.UserData;
import ru.sixbeans.meetingengine.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public Set<UserData> getAllUsers() {
        return new HashSet<>(userRepository.findAll().stream().map(userMapper::map).toList());
    }

    public UserData getUserByPrincipal(OidcUser principal) {
        String provider = principal.getIssuer().getHost();
        String subject = principal.getSubject();
        return userMapper.map(userRepository.findByProviderAndSubject(provider, subject)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException(subject)));
    }

    public UserData getUserByUsername(String userName) {
        var user = userRepository.findByUserName(userName);
        if (user.isEmpty() || !userName.startsWith("@"))
            throw new UserNotFoundException(userName);
        else return user.map(userMapper::map)
                .orElseThrow(() -> new UserNotFoundException(userName));
    }

    public UserData getUserById(long userId) {
        return userRepository.findById(userId).map(userMapper::map)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public UserData getUserById(long userId, long userIdFromUrl) {
        // If these users have mutual subscriptions or id of authorized user equals id from URL
        if ((isSubscription(userId, userIdFromUrl) && isSubscriber(userId, userIdFromUrl)) || (userId == userIdFromUrl)) {
            return getUserById(userIdFromUrl);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. You do not have permission to access this resource.");
        }
    }

    public PersonalInfoData getUserPersonalInfoById(long userId) {
        return userRepository.findById(userId).map(User::getPersonalInfo)
                .map(userMapper::map).orElseThrow(() -> new UserNotFoundException(userId));
    }

    public List<UserData> findAllUserSubscriptions(long userId) {
        return userRepository.findById(userId).map(User::getSubscriptions)
                .map(userMapper::map).orElseThrow(() -> new UserNotFoundException(userId));
    }

    public List<UserData> findAllUserSubscribers(long userId) {
        return userRepository.findById(userId).map(User::getSubscribers)
                .map(userMapper::map).orElseThrow(() -> new UserNotFoundException(userId));
    }

    public boolean isSubscriber(long userId, long subscriberId) {
        return userRepository.existsByIdAndSubscribers_Id(userId, subscriberId);
    }

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
                .map(userMapper::map)
                .toList();
    }
}
