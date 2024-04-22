package ru.sixbeans.meetingengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sixbeans.meetingengine.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
