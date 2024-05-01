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
        Optional<User> optionalUser = userRepository.findByProviderAndExternalId(provider, externalId);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User with the token was not found");
        }

        return optionalUser.get();
    }

    public User findByUsername(String userName) {
        Optional<User> optionalUser = userRepository.findByUserName(userName);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User with username = " + userName + " was not found");
        }

        return optionalUser.get();
    }

    public User findByUserId(long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User with id = " + userId + " was not found");
        }

        return optionalUser.get();
    }

    public Set<User> findAll() {
        List<User> userList = userRepository.findAll();

        return new HashSet<>(userList);
    }

    public Set<User> findUserFriends(String userName) {
        Optional<User> optionalUser = userRepository.findByUserName(userName);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User with username = " + userName + " was not found");
        }

        return optionalUser.get().getFriends();
    }

    public boolean isFriend(long userId, long friendId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<User> optionalFriend = userRepository.findById(friendId);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User with user with id = " + userId + " not found");
        }

        if (optionalFriend.isEmpty()) {
            throw new UsernameNotFoundException("Friend with id = " + friendId + " not found");
        }

        Hibernate.initialize(optionalUser.get().getFriends());
        return optionalUser.get().getFriends().contains(optionalFriend.get());
    }

    @Transactional
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public void addFriend(long userId, long friendId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<User> optionalFriend = userRepository.findById(friendId);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User with user with id = " + userId + " not found");
        }

        if (optionalFriend.isEmpty()) {
            throw new UsernameNotFoundException("Friend with id = " + friendId + " not found");
        }

        Hibernate.initialize(optionalUser.get().getFriends());
        optionalUser.get().getFriends().add(optionalFriend.get());
    }

    @Transactional
    public void removeFriend(long userId, long friendId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<User> optionalFriend = userRepository.findById(friendId);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User with user with id = " + userId + " not found");
        }

        if (optionalFriend.isEmpty()) {
            throw new UsernameNotFoundException("Friend with id = " + friendId + " not found");
        }

        Hibernate.initialize(optionalUser.get().getFriends());
        optionalUser.get().getFriends().remove(optionalFriend.get());
    }

    @Transactional
    public void addTag(long userId, long tagId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Tag> optionalTag = tagRepository.findById(tagId);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User with id = " + userId + " not found");
        }

        if (optionalTag.isEmpty()) {
            throw new TagNotFoundException("Tag with id = " + tagId + " not found");
        }

        Hibernate.initialize(optionalUser.get().getTags());
        optionalUser.get().getTags().add(optionalTag.get());

        Hibernate.initialize(optionalTag.get().getUsers());
        optionalTag.get().getUsers().add(optionalUser.get());
    }

    @Transactional
    public void removeTag(long userId, long tagId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Tag> optionalTag = tagRepository.findById(tagId);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User with id = " + userId + " not found");
        }

        if (optionalTag.isEmpty()) {
            throw new TagNotFoundException("Tag with id = " + tagId + " not found");
        }

        Hibernate.initialize(optionalTag.get().getUsers());
        optionalTag.get().getUsers().remove(optionalUser.get());

        Hibernate.initialize(optionalUser.get().getTags());
        optionalUser.get().getTags().remove(optionalTag.get());
    }
}