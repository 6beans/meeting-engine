package ru.sixbeans.meetingengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sixbeans.meetingengine.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
