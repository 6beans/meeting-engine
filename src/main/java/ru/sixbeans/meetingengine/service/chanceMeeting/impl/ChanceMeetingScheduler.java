package ru.sixbeans.meetingengine.service.chanceMeeting.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChanceMeetingScheduler {

    private final ChanceMeetingService chanceMeetingService;

    @Scheduled(cron = "00 00 00 * * *") // Every day at midnight
    public void createEventsAndChangeStatuses() {
        chanceMeetingService.changeUsersChanceMeetingStatus(ChanceMeetingStatus.REGISTERED, ChanceMeetingStatus.ENROLLED);
        chanceMeetingService.createChanceMeetingPairsOfUsers();
        chanceMeetingService.changeUsersChanceMeetingStatus(ChanceMeetingStatus.PENDING_REMOVING, ChanceMeetingStatus.NOT_REGISTERED);
    }
}
