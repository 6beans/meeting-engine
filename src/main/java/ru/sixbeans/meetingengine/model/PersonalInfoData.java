package ru.sixbeans.meetingengine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import ru.sixbeans.meetingengine.entity.PersonalInfo;

import java.io.Serializable;

/**
 * DTO for {@link PersonalInfo}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PersonalInfoData(Long id,
                               @NotBlank String fullName,
                               String instagram,
                               String telegram,
                               String github,
                               String gitlab,
                               String phone,
                               String vk)
        implements Serializable {

}
