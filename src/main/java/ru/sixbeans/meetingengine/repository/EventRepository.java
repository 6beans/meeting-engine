package ru.sixbeans.meetingengine.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.sixbeans.meetingengine.entity.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByIsActive(boolean isActive);

    List<Event> findAllByOwnerId(String id);

    Page<Event> findAllByOwnerId(String ownerId, Pageable pageable);
}
