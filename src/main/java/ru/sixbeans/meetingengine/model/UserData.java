package ru.sixbeans.meetingengine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.sixbeans.meetingengine.entity.User;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserData(String id, String username,
                       String firstName, String secondName,
                       String email, String about)
        implements Serializable {

}
