package ru.sixbeans.meetingengine.aspect;

import jakarta.persistence.EntityNotFoundException;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.sixbeans.meetingengine.exception.UserNotFoundException;

@Aspect
@Component
public class UserServiceExceptionAspect {

    @AfterThrowing(pointcut = "execution(* ru.sixbeans.meetingengine.service.user.impl.UserService.*(..))", throwing = "e")
    public void handleEntityNotFoundException(EntityNotFoundException e) {
        if (e instanceof UserNotFoundException) return;
        throw new UserNotFoundException("User not found", e);
    }
}
