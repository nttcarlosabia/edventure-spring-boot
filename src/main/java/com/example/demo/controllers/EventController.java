package com.example.demo.controllers;

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
import com.example.demo.repositories.EventRepository;

@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:5173/*", "https://edventure-six.vercel.app" })
@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @GetMapping
    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event request) {
        try {
            Event newEvent = eventRepository.save(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(newEvent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        try {
            Optional<Event> event = eventRepository.findById(id);
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
            Optional<Event> existingEventOptional = eventRepository.findById(id);
            if (existingEventOptional.isPresent()) {
                Event existingEvent = existingEventOptional.get();
                if (request.getUserOwner() != null) {
                    existingEvent.setUserOwner(request.getUserOwner());
                }
                if (request.getName() != null) {
                    existingEvent.setName(request.getName());
                }
                if (request.getType() != null) {
                    existingEvent.setType(request.getType());
                }
                if (request.getDescription() != null) {
                    existingEvent.setDescription(request.getDescription());
                }
                if (request.getImage() != null) {
                    existingEvent.setImage(request.getImage());
                }
                if (request.getDate() != null) {
                    existingEvent.setDate(request.getDate());
                }
                if (request.getAddress() != null) {
                    existingEvent.setAddress(request.getAddress());
                }
                if (request.getPlaceId() != null) {
                    existingEvent.setPlaceId(request.getPlaceId());
                }
                if (request.getAssistants() != null) {
                    existingEvent.setAssistants(request.getAssistants());
                }
                Event updatedEvent = eventRepository.save(existingEvent);
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
            eventRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Event deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete event");
        }
    }
}
