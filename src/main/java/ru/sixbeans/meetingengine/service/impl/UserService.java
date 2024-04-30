package ru.sixbeans.meetingengine.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sixbeans.meetingengine.entity.Tag;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Optional<User> findByToken(OAuth2AuthenticationToken token) {
        var provider = token.getAuthorizedClientRegistrationId();
        var externalId = token.getPrincipal().getName();
        return userRepository.findByProviderAndExternalId(provider, externalId);
    }

    public void addTag(User user, Tag tag) {
        tag.getUsers().add(user);
        user.getTags().add(tag);
    }

    public void removeTag(User user, Tag tag) {
        tag.getUsers().remove(user);
        user.getTags().remove(tag);
    }

    public void addFriend(User a, User b) {
        a.getFriends().add(b);
        b.getFriends().add(a);
    }

    public void removeFriend(User a, User b) {
        a.getFriends().remove(b);
        b.getFriends().remove(a);
    }
}
