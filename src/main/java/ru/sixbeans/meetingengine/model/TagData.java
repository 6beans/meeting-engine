package ru.sixbeans.meetingengine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import ru.sixbeans.meetingengine.entity.Tag;

import java.io.Serializable;

/**
 * DTO for {@link Tag}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TagData(Long id,
                      @NotBlank String title,
                      @NotBlank String category)
        implements Serializable {

}
