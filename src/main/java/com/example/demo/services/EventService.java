package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.models.Event;
import com.example.demo.repositories.EventRepository;

@Service
public class EventService {

    private EventRepository eventRepo;

    @Autowired
    public EventService(EventRepository eventRepo) {
        this.eventRepo = eventRepo;

    }

    public List<Event> getEvents() {
        return eventRepo.findAll();
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepo.findById(id);
    }

    public void deleteById(Long id) {
        eventRepo.deleteById(id);
    }

    public Event saveEvent(Event event) {
        return eventRepo.save(event);
    }

}
