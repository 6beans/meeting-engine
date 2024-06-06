package ru.sixbeans.meetingengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sixbeans.meetingengine.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, String> {

}
