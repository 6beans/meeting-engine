package ru.sixbeans.meetingengine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import ru.sixbeans.meetingengine.entity.Event;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link Event}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record EventData(Long id, String ownerId,
                        @NotBlank String title,
                        String description,
                        LocalDate creationDate,
                        LocalDate endDate,
                        Boolean isActive)
        implements Serializable {

}
