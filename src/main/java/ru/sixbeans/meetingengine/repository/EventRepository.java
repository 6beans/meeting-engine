package ru.sixbeans.meetingengine.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.sixbeans.meetingengine.entity.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByIsActive(boolean isActive);

    List<Event> findAllByOwnerId(String id);

    Page<Event> findAllByOwnerId(String ownerId, Pageable pageable);

    @Query("SELECT e FROM Event e JOIN e.tags t WHERE t.id IN :tags GROUP BY e ORDER BY COUNT(t) DESC")
    List<Event> findMostSuitableEventsByTags(@Param("tags") List<String> tags);
}
