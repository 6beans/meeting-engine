package ru.sixbeans.meetingengine.service.impl.user;

import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sixbeans.meetingengine.entity.Tag;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.exception.TagNotFoundException;
import ru.sixbeans.meetingengine.repository.TagRepository;
import ru.sixbeans.meetingengine.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    @Autowired
    public UserService(UserRepository userRepository, TagRepository tagRepository) {
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
    }

    public User findByToken(OAuth2AuthenticationToken token) {
        var provider = token.getAuthorizedClientRegistrationId();
        var externalId = token.getPrincipal().getName();

        return userRepository.findByProviderAndExternalId(provider, externalId)
                .orElseThrow(() -> new UsernameNotFoundException("User with the token was not found"));
    }

    public User findByUsername(String name) {
        return userRepository.findByUserName(name)
                .orElseThrow(() -> new UsernameNotFoundException("User with username = " + name + " was not found"));
    }

    public User findByUserId(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with username = " + id + " was not found"));
    }

    public Set<User> findAll() {
        return new HashSet<>(userRepository.findAll());
    }

    public Set<User> findUserFriends(String name) {
        return findByUsername(name).getFriends();
    }

    public boolean isFriend(long userId, long friendId) {
        User user = findByUserId(userId);
        User friend = findByUserId(friendId);

        Hibernate.initialize(user.getFriends());

        return user.getFriends().contains(friend);
    }


    public Set<User> getFullFriendsByUserName(String name) {
        User user = findByUsername(name);

        return findByUsername(name).getFriends()
                .stream()
                .peek(friend -> Hibernate.initialize(friend.getFriends()))
                .filter((friend) -> friend.getFriends().contains(user))
                .collect(Collectors.toSet());

    }

    @Transactional
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public void addFriend(long userId, long friendId) {
        User user = findByUserId(userId);
        User friend = findByUserId(friendId);
        user.getFriends().add(friend);
    }

    @Transactional
    public void removeFriend(long userId, long friendId) {
        User user = findByUserId(userId);
        User friend = findByUserId(friendId);
        user.getFriends().remove(friend);
    }

    @Transactional
    public void addTag(long userId, long tagId) {
        User user = findByUserId(userId);
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException("Tag with id = " + tagId + " not found"));

        user.getTags().add(tag);
        tag.getUsers().add(user);
    }

    @Transactional
    public void removeTag(long userId, long tagId) {
        User user = findByUserId(userId);
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException("Tag with id = " + tagId + " not found"));

        user.getTags().remove(tag);
        tag.getUsers().remove(user);
    }
}