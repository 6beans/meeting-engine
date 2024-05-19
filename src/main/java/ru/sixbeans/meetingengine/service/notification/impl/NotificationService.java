package ru.sixbeans.meetingengine.service.notification.impl;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.event.UserSubscribedUser;
import ru.sixbeans.meetingengine.service.email.impl.EmailService;

import java.io.UnsupportedEncodingException;

@Component
@AllArgsConstructor
public class NotificationService {
    private final EmailService emailService;


    @EventListener
    @Async
    public void handleUserLikedEvent(UserSubscribedUser event) {
        try {
            sendEmailNotification(event.getSubscribed(), event.getSubscriber());
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendEmailNotification(User liked, User liker) throws MessagingException, UnsupportedEncodingException {
        String subject = "Someone liked you! 0_0";
        String text = "<p> u are liked by " + "<a href='тут типо ссылка на пользователя'>" + liker.getPersonalInfo().getFullName() + "</a> <p>";

        emailService.sendHtmlMessage(liked.getEmail(), subject, text);
    }
}