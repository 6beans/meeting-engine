package ru.sixbeans.meetingengine.service.impl;

import org.springframework.stereotype.Service;
import ru.sixbeans.meetingengine.entity.Tag;
import ru.sixbeans.meetingengine.entity.User;

@Service
public class UserService {

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
