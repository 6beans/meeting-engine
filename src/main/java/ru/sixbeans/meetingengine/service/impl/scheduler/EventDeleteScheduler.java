package ru.sixbeans.meetingengine.service.impl.scheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.sixbeans.meetingengine.service.impl.event.EventService;

import java.time.LocalDate;

@Component
public class EventDeleteScheduler {


    private final EventService eventService;

    public EventDeleteScheduler(EventService eventService) {
        this.eventService = eventService;
    }


    @Scheduled(cron = "0 0 0 * * *")  // Ежедневно в полночь
    public void cleanupEvents() {
        eventService.deleteAllByEndDateAfter(LocalDate.now());
    }
}