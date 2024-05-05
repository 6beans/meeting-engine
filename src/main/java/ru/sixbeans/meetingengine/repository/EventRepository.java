package ru.sixbeans.meetingengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.sixbeans.meetingengine.entity.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByIdAndIsActive(long id, boolean isActive);

    List<Event> findByIsActive(boolean isActive);

    @Modifying
    @Query("DELETE FROM Event e WHERE e.endDate < :thresholdDate")
    void deleteEventsOlderThan(LocalDate thresholdDate);
}
