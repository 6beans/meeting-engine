package ru.sixbeans.meetingengine.controller;

import lombok.AllArgsConstructor;
import org.springframework.expression.AccessException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sixbeans.meetingengine.entity.Event;
import ru.sixbeans.meetingengine.model.EventData;
import ru.sixbeans.meetingengine.model.UserData;
import ru.sixbeans.meetingengine.service.impl.event.EventService;
import ru.sixbeans.meetingengine.service.impl.user.UserService;

import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;
    private final UserService userService;

    @GetMapping("/active")
    public String getAllActiveEvents(Model model) {
        model.addAttribute("events", eventService.findByIsActive(true)
                .stream()
                .map(EventData::from)
                .collect(Collectors.toList()));

        return "events/feed";
    }

    @GetMapping("/participate/active")
    public String getAllMyParticipateAndActiveEvents(Model model, OAuth2AuthenticationToken token) {
        model.addAttribute("events", eventService.findByMemberTokenAndIsActive(token, true)
                .stream()
                .map(EventData::from)
                .collect(Collectors.toList()));

        return "events/feed";
    }

    @GetMapping("/participate/inactive")
    public String getAllMyParticipateAndInactiveEvents(Model model, OAuth2AuthenticationToken token) {
        model.addAttribute("events", eventService.findByMemberTokenAndIsActive(token, false)
                .stream()
                .map(EventData::from)
                .collect(Collectors.toList()));

        return "events/feed";
    }

    @GetMapping("/owned/active")
    public String getAllMyOwnedAndActiveEvents(Model model, OAuth2AuthenticationToken token) {
        model.addAttribute("events", eventService.findByOwnerTokenAndIsActive(token, true)
                .stream()
                .map(EventData::from)
                .collect(Collectors.toList()));

        return "events/feed";
    }

    @GetMapping("/owned/inactive")
    public String getAllMyOwnedAndInactiveEvents(Model model, OAuth2AuthenticationToken token) {
        model.addAttribute("events", eventService.findByOwnerTokenAndIsActive(token, false)
                .stream()
                .map(EventData::from)
                .collect(Collectors.toList()));

        return "events/feed";
    }

    @GetMapping("/{id}")
    public String getCertainEvent(Model model, @PathVariable long id) {
        model.addAttribute("event", EventData.from(eventService.findById(id)));
        return "events/id";
    }

    @GetMapping("/new")
    public String newEvent(@ModelAttribute("event") Event event) {
        return "events/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("event") Event event, OAuth2AuthenticationToken token) {
        eventService.save(event, token);
        return "redirect:/events";
    }

    @PostMapping("{eventId}/delete_member/{userId}")
    public String deleteMember(@PathVariable long eventId, @PathVariable long userId, OAuth2AuthenticationToken token) throws AccessException {
        eventService.deleteMemberByUserId(eventId, userId, token);
        return "redirect:/events/" + eventId;
    }

    @PostMapping("{eventId}/join")
    public String joinToEvent(@PathVariable long eventId, OAuth2AuthenticationToken token) {
        eventService.joinToEvent(token, eventId);
        return "redirect:/events/" + eventId;
    }


    @PostMapping("{eventId}/add_tag/{tagTitle}")
    public String addTag(@PathVariable long eventId, @PathVariable String tagTitle) {
        eventService.addTagByTagTitle(eventId, tagTitle);
        return "redirect:/events/" + eventId;
    }

    @PostMapping("{eventId}/delete_tag/{tagTitle}")
    public String deleteTag(@PathVariable long eventId, @PathVariable String tagTitle) {
        eventService.deleteTagByTagTitle(eventId, tagTitle);
        return "redirect:/events/" + eventId;
    }

}
