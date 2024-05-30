package ru.sixbeans.meetingengine.service.notification.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.sixbeans.meetingengine.event.UserSubscribedEvent;
import ru.sixbeans.meetingengine.service.email.impl.EmailService;
import ru.sixbeans.meetingengine.service.user.impl.UserService;

@Component
@RequiredArgsConstructor
public class EmailNotificationListener {

    private final EmailService emailService;
    private final UserService userService;

    @Async
    @SneakyThrows
    @EventListener
    public void sendEmailNotification(UserSubscribedEvent event) {
        var userData = userService.getUser(event.getSubscriberId());
        var subscriberFullName = userData.fullName();
        var email = userData.email();
        String subject = "Someone liked you! 0_0";
        String text = """
                <p>
                u are liked by <a href=''>%s</a>
                <p>""".formatted(subscriberFullName);
        emailService.sendHtmlMessage(email, subject, text);
    }
}
