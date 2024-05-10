package ru.sixbeans.meetingengine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import ru.sixbeans.meetingengine.entity.User;

import java.io.Serializable;
import java.util.Collection;

/**
 * DTO for {@link User}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserData(Long id,
                       @NotBlank String userName,
                       @NotBlank String email,
                       byte[] avatar, String profileDescription,
                       Collection<TagData> tagDTOs,
                       Collection<Long> eventIds,
                       Collection<Long> memberEventIds)
        implements Serializable {
}
