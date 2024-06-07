package ru.sixbeans.meetingengine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.sixbeans.meetingengine.entity.Event;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for {@link Event}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record EventData(Long id, String ownerId,
                        String title, String about,
                        LocalDateTime startDate,
                        LocalDateTime endDate,
                        Boolean isActive)
        implements Serializable {

    public static EventData of(String ownerId) {
        return new EventData(null, ownerId, "", "", null, null, true);
    }
}
