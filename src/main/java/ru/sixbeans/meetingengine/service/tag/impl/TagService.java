package ru.sixbeans.meetingengine.service.tag.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sixbeans.meetingengine.entity.Tag;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.repository.TagRepository;
import ru.sixbeans.meetingengine.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Collection<TagData> findAllUserTags(long userId) {
        return userRepository.findById(userId)
                .map(User::getTags).map(tagMapper::map)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Transactional
    public void addTagToUser(long userId, long tagId) {
        User user = userRepository.getReferenceById(userId);
        Tag tag = tagRepository.getReferenceById(tagId);
        user.getTags().add(tag);
        tag.getUsers().add(user);
    }

    @Transactional
    public void removeTagFromUser(long userId, long tagId) {
        User user = userRepository.getReferenceById(userId);
        Tag tag = tagRepository.getReferenceById(tagId);
        user.getTags().remove(tag);
        tag.getUsers().remove(user);
    }
}
