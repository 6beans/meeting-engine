package ru.sixbeans.meetingengine.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.sixbeans.meetingengine.entity.User;

@Getter
public class UserSubscribedUser extends ApplicationEvent {
    private final User subscriber;
    private final User subscribed;

    public UserSubscribedUser(Object source, User subscriber, User subscribed) {
        super(source);
        this.subscriber = subscriber;
        this.subscribed = subscribed;
    }

}
