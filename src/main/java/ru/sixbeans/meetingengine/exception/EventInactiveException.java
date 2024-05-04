package ru.sixbeans.meetingengine.exception;

public class EventInactiveException extends RuntimeException {
    public EventInactiveException(String message) {
        super(message);
    }
}
