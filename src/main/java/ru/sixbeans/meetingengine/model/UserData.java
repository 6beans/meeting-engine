package ru.sixbeans.meetingengine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import ru.sixbeans.meetingengine.entity.User;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserData(Long id,
                       @NotBlank String email,
                       @NotBlank String userName,
                       @NotBlank String fullName,
                       String profileDescription)
        implements Serializable {

}
