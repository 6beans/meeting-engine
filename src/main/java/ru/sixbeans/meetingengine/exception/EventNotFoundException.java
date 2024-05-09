package ru.sixbeans.meetingengine.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("unused")
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class EventNotFoundException extends EntityNotFoundException {

    public EventNotFoundException() {
    }

    public EventNotFoundException(long id) {
        super("User with id " + id + " not found");
    }

    public EventNotFoundException(Exception cause) {
        super(cause);
    }

    public EventNotFoundException(String message) {
        super(message);
    }

    public EventNotFoundException(String message, Exception cause) {
        super(message, cause);
    }
}
