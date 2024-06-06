package ru.sixbeans.meetingengine.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("unused")
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException() {

    }

    public UserNotFoundException(String userId) {
        super("User with id " + userId + " not found");
    }

    public UserNotFoundException(Exception cause) {
        super(cause);
    }

    public UserNotFoundException(String message, Exception cause) {
        super(message, cause);
    }
}
