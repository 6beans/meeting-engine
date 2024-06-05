package ru.sixbeans.meetingengine.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.sixbeans.meetingengine.entity.Event;

import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByIsActive(boolean isActive);

    List<Event> findAllByOwnerId(long id);

    Page<Event> findAllByOwnerId(Long ownerId, Pageable pageable);

    @Query(value = """
            SELECT events.*
            FROM public.tag_events
            JOIN events ON tag_events.events_id = events.id
            WHERE tag_events.tags_id IN (SELECT tags_id FROM tag_users WHERE users_id = :userId)
            AND events.is_active = true
            GROUP BY events.id
            ORDER BY COUNT(tag_events.tags_id)
            DESC""",
            countQuery = """
            SELECT events.*
            FROM public.tag_events
            JOIN events ON tag_events.events_id = events.id
            WHERE tag_events.tags_id IN (SELECT tags_id FROM tag_users WHERE users_id = :userId)
            AND events.is_active = true
            """, nativeQuery = true)
    Page<Event> getRecommendedEvents(@Param("userId") Long userId, Pageable pageable);

    @Query(value = """
            SELECT events.*
            FROM events
            JOIN tag_events ON events.id = tag_events.events_id
            WHERE tag_events.tags_id IN :tags
            GROUP BY events.id
            ORDER BY COUNT(tag_events.tags_id)
            DESC""",
            countQuery = """
            SELECT COUNT(DISTINCT events.id)
            FROM events
            JOIN tag_events ON events.id = tag_events.events_id
            WHERE tag_events.tags_id IN :tags
            """, nativeQuery = true)
    Page<Event> filterEventsByTags(@Param("tags") Set<Integer> tags, Pageable pageable);
}
