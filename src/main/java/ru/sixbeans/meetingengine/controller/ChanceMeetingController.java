package ru.sixbeans.meetingengine.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.sixbeans.meetingengine.service.chanceMeeting.impl.ChanceMeetingService;
import ru.sixbeans.meetingengine.service.chanceMeeting.impl.ChanceMeetingStatus;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chance_meeting")
public class ChanceMeetingController {
    private final ChanceMeetingService chanceMeetingService;

    @GetMapping
    public String home(HttpSession session, Model model) {
        long userId = (long) session.getAttribute("userId");
        ChanceMeetingStatus status = chanceMeetingService.getChanceMeetingStatus(userId);

        switch (status) {
            case NOT_REGISTERED -> {
                return "chance_meeting_not_registered";
            }
            case REGISTERED -> {
                return "chance_meeting_not_enrolled";
            }
            case ENROLLED, PENDING_REMOVING -> {
                model.addAttribute("user", chanceMeetingService.getUserPartner(userId));
                model.addAttribute("userPersonalInfo", chanceMeetingService.getUserPartnerPersonalInfo(userId));
                return "chance_meeting_with_random_user";
            }
            // TODO: What kind of exception should I throw?
            default -> throw new RuntimeException();
        }
    }

    @PostMapping
    public String registerToChanceMeeting(HttpSession session){
        long userId = (long) session.getAttribute("userId");
        ChanceMeetingStatus status = chanceMeetingService.getChanceMeetingStatus(userId);

        if (status == ChanceMeetingStatus.NOT_REGISTERED) {
            chanceMeetingService.changeUserChanceMeetingStatus(userId, ChanceMeetingStatus.REGISTERED);
        }

        return "redirect:/chance_meeting";
    }

    @DeleteMapping
    public String cancelChanceMeetingParticipation(HttpSession session) {
        long userId = (long) session.getAttribute("userId");
        ChanceMeetingStatus status = chanceMeetingService.getChanceMeetingStatus(userId);

        switch (status) {
            case ENROLLED -> chanceMeetingService.changeUserChanceMeetingStatus(userId, ChanceMeetingStatus.PENDING_REMOVING);
            case REGISTERED -> chanceMeetingService.changeUserChanceMeetingStatus(userId, ChanceMeetingStatus.NOT_REGISTERED);
        }

        return "redirect:/chance_meeting";
    }
}