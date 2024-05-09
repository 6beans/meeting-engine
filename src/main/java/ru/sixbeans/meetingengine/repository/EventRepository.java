package ru.sixbeans.meetingengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.sixbeans.meetingengine.entity.Event;

import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByIsActive(boolean isActive);

    List<Event> findAllByOwnerId(long id);

    @Query(value = "SELECT events.*" +
            "FROM tag_events" +
            "JOIN events ON tag_events.events_id = events.id" +
            "WHERE tag_events.tags_id IN (SELECT tags_id FROM tag_events WHERE events_id = :eventId)" +
            "AND tag_events.events_id != :eventId" +
            "GROUP BY events.id" +
            "ORDER BY  COUNT(tag_events.tags_id) DESC", nativeQuery = true)
    List<Event> getRecommendedEvents(@Param("eventId") Long eventId);

    @Query(value = "SELECT events.*" +
            "FROM events" +
            "JOIN tag_events ON events.id = tag_events.events_id" +
            "WHERE tag_events.tags_id IN :tags" +
            "GROUP BY events.id" +
            "ORDER BY COUNT(tag_events.tags_id) DESC", nativeQuery = true)
    List<Event> filterEventsByTags(@Param("tags") Set<Integer> tags);

}
