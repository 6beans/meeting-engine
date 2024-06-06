package ru.sixbeans.meetingengine.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserSubscribedEvent extends ApplicationEvent {

    private final String subscriberId;
    private final String subscriptionId;

    public UserSubscribedEvent(Object source, String subscriberId, String subscriptionId) {
        super(source);
        this.subscriberId = subscriberId;
        this.subscriptionId = subscriptionId;
    }
}
