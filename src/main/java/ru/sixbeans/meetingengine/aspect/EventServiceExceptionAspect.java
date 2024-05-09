package ru.sixbeans.meetingengine.aspect;

import jakarta.persistence.EntityNotFoundException;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.sixbeans.meetingengine.exception.EventNotFoundException;

@Aspect
@Component
public class EventServiceExceptionAspect {

    @AfterThrowing(pointcut = "execution(* ru.sixbeans.meetingengine.service.event.impl.EventService.*(..))", throwing = "e")
    public void handleEntityNotFoundException(EntityNotFoundException e) {
        if (e instanceof EventNotFoundException) return;
        throw new EventNotFoundException("Event not found", e);
    }
}
