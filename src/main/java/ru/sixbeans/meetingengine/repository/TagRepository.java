package ru.sixbeans.meetingengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sixbeans.meetingengine.entity.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByTitle(String title);
}
