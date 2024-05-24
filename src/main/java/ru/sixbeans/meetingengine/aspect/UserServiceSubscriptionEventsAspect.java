package ru.sixbeans.meetingengine.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.sixbeans.meetingengine.event.UserSubscribedEvent;

@Aspect
@Component
@Profile("default")
@RequiredArgsConstructor
public class UserServiceSubscriptionEventsAspect {

    private final ApplicationEventPublisher publisher;

    @AfterReturning(pointcut = "execution(* ru.sixbeans.meetingengine.service.user.impl.UserService.subscribe(..))")
    public void handleEntityNotFoundException(JoinPoint jp) {
        var event = new UserSubscribedEvent(
                jp.getThis(),
                (Long) jp.getArgs()[0],
                (Long) jp.getArgs()[1]
        );
        publisher.publishEvent(event);
    }
}
