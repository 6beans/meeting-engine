package ru.sixbeans.meetingengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sixbeans.meetingengine.entity.DeletedEvent;

public interface DeletedEventRepository extends JpaRepository<DeletedEvent, Long> {
}
