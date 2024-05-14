package ru.sixbeans.meetingengine.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sixbeans.meetingengine.model.EventData;
import ru.sixbeans.meetingengine.service.event.impl.EventService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @GetMapping
    public String events(Model model, HttpSession session,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size) {
        long userId = (long) session.getAttribute("userId");
        Page<EventData> eventPage = eventService.findAllEventsByOwnerId(userId, PageRequest.of(page, size));
        model.addAttribute("eventPage", eventPage);
        return "events";
    }

    @GetMapping("/new")
    public String newEventForm(Model model, HttpSession session) {
        long userId = (long) session.getAttribute("userId");
        model.addAttribute("event", new EventData(null, userId, "", "", null, null, true));
        return "new-event";
    }

    @PostMapping
    public String createEvent(@ModelAttribute EventData event, HttpSession session) {
        long userId = (long) session.getAttribute("userId");
        eventService.addEventToUser(event, userId);
        return "redirect:/events";
    }
}
