package ru.sixbeans.meetingengine.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserSubscribedEvent extends ApplicationEvent {

    private final long subscriberId;
    private final long subscriptionId;

    public UserSubscribedEvent(Object source, long subscriberId, long subscriptionId) {
        super(source);
        this.subscriberId = subscriberId;
        this.subscriptionId = subscriptionId;
    }
}
