package ru.sixbeans.meetingengine.service.event.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InactiveEventsScheduler {

    private final EventService eventService;

    @Scheduled(cron = "0 0 0 * * *") // Every day at midnight
    public void cleanupEvents() {
        eventService.updateEventsActivity(LocalDateTime.now());
    }
}
