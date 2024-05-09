package ru.sixbeans.meetingengine.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("unused")
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class TagNotFoundException extends EntityNotFoundException {

    public TagNotFoundException() {
    }

    public TagNotFoundException(long id) {
        super("Tag with id " + id + " not found");
    }

    public TagNotFoundException(Exception cause) {
        super(cause);
    }

    public TagNotFoundException(String message) {
        super(message);
    }

    public TagNotFoundException(String message, Exception cause) {
        super(message, cause);
    }
}
