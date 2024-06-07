package ru.sixbeans.meetingengine.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sixbeans.meetingengine.mapper.EventMapper;
import ru.sixbeans.meetingengine.model.EventData;
import ru.sixbeans.meetingengine.repository.EventRepository;
import ru.sixbeans.meetingengine.service.event.impl.EventService;
import ru.sixbeans.meetingengine.service.tag.impl.TagService;
import ru.sixbeans.meetingengine.service.user.impl.UserService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final EventService eventService;
    private final UserService userService;
    private final TagService tagService;

    @GetMapping
    public String events(Model model, @AuthenticationPrincipal OidcUser user) {
        var events = eventService.findAllEventsByOwnerId(user.getSubject());
        var eventsTags = events.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        k -> tagService.findAllEventTags(k.id())
                ));
        var eventsPictures = eventService.findAllEventsByOwnerId(user.getSubject()).stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        k -> eventService.getEventPicture(k.id())
                ));
        model.addAttribute("events", events);
        model.addAttribute("eventsTags", eventsTags);
        model.addAttribute("eventsPictures", eventsPictures);
        return "events";
    }

    @GetMapping("/new")
    public String newEventForm(@AuthenticationPrincipal OidcUser principal, Model model) {
        String avatarBase64 = Base64.getEncoder().encodeToString(rand());
        model.addAttribute("event", EventData.of(principal.getSubject()));
        model.addAttribute("picture", avatarBase64);
        model.addAttribute("tags", "");
        return "edit-event";
    }

    @SneakyThrows
    @PostMapping("/new")
    public String newEventForm(
            @ModelAttribute EventData eventData,
            @RequestParam(value = "tags", required = false) List<String> tags,
            @RequestParam(value = "picture", required = false) MultipartFile picture,
            @AuthenticationPrincipal OidcUser principal) {
        long id = eventRepository.save(eventMapper.map(eventData))
                .getId();
        tagService.updateEventTags(id, tags);
        eventService.updateEventPicture(id, picture.getBytes());
        eventService.addEventToUser(eventData, principal.getSubject());
        return "redirect:/events";
    }

    @SneakyThrows
    private byte[] rand() {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        Color randomColor = new Color(red, green, blue);

        image.setRGB(0, 0, randomColor.getRGB());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }
}
