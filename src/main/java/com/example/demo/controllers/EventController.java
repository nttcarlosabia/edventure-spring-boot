package com.example.demo.controllers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.Event;
import com.example.demo.models.User;
import com.example.demo.services.EventService;
import com.example.demo.services.UserService;
import com.example.demo.utils.Utils;

@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:5173/*", "https://edventure-six.vercel.app" })
@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final UserService userService;

    @Autowired
    public EventController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }
    @GetMapping
    public List<Event> getEvents() {
        return eventService.getEvents();
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event request) {
        try {
            request.setFollowersHistory(new LinkedHashMap<>());
            String currentDate = Utils.getCurrentDateAsString();
            request.getFollowersHistory().put(currentDate, 0);

            Event newEvent = eventService.saveEvent(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(newEvent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        try {
            Optional<Event> event = eventService.getEventById(id);
            if (event.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(event.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event request) {
        try {
            Optional<Event> existingEventOptional = eventService.getEventById(id);
            if (existingEventOptional.isPresent()) {
                Event existingEvent = existingEventOptional.get();
                Utils.updateEventFields(existingEvent, request);
                Event updatedEvent = eventService.saveEvent(existingEvent);
                return ResponseEntity.status(HttpStatus.OK).body(updatedEvent);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        try {
            Optional<Event> optionalEvent = eventService.getEventById(id);
            if (!optionalEvent.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
            }
            Event event = optionalEvent.get();

            for (User user : event.getUsersFollowing()) {
                user.getFollowingEvents().remove(event);
                userService.saveUser(user);
            }

            eventService.deleteById(id);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Event deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete event");
        }
    }
}
