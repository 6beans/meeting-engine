package ru.sixbeans.meetingengine.service.impl.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.sixbeans.meetingengine.service.impl.event.EventService;

import java.time.LocalDate;

@Component
public class InactiveEventsScheduler {


    private final EventService eventService;

    public InactiveEventsScheduler(EventService eventService) {
        this.eventService = eventService;
    }


    @Scheduled(cron = "0 0 1 * * *")  // Ежедневно в полночь
    public void cleanupEvents() {
        eventService.changeConditionAllEventsByEndDate(LocalDate.now());
        eventService.deleteEventsOlderThan(LocalDate.now().minusMonths(1));
    }
}