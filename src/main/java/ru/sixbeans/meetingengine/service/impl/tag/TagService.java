package ru.sixbeans.meetingengine.service.impl.tag;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sixbeans.meetingengine.entity.Tag;
import ru.sixbeans.meetingengine.exception.TagNotFoundException;
import ru.sixbeans.meetingengine.repository.TagRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class TagService {
    private final TagRepository tagRepository;


    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public Tag findByTitle(String title) throws TagNotFoundException {
        return tagRepository.findByTitle(title)
                .orElseThrow(() -> new TagNotFoundException("Tag with title =" + title + " not found"));
    }

    public Tag findById(long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag with id =" + id + " not found"));
    }

    @Transactional
    public void update(long id, Tag newTag) {
        newTag.setId(id);
        tagRepository.save(newTag);
    }

    @Transactional
    public void save(Tag tag) {
        tagRepository.save(tag);
    }
}
