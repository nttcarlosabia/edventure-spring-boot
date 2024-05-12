package com.example.demo.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.sql.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.demo.models.Event;
import com.example.demo.models.User;

import com.example.demo.services.EventService;
import com.example.demo.services.UserService;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private EventService eventService;

    @Mock
    private UserService userService;

    @InjectMocks
    private EventController eventController;

    private List<User> users;
    private List<Event> events;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
        users = new ArrayList<>();
        users.add(new User(1L, "user1", "John", "Doe", "john@example.com", "avatar1.jpg",
                new Date(System.currentTimeMillis())));
        users.add(new User(2L, "user2", "Jane", "Doe", "jane@example.com", "avatar2.jpg",
                new Date(System.currentTimeMillis())));

        events = new ArrayList<>();
        events.add(new Event(users.get(0), "Event 1", "Type 1", "Description 1", "Place 1", "image1.jpg",
                "2035-06-01T08:30", "Address 1", "Assistants 1"));
        events.add(new Event(users.get(1), "Event 2", "Type 2", "Description 2", "Place 2", "image2.jpg",
                "2035-06-01T08:30", "Address 2", "Assistants 2"));
    }

    @Test
    void testGetEvents() throws Exception {
        when(eventService.getEvents()).thenReturn(events);

        mockMvc.perform(MockMvcRequestBuilders.get("/events")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(events.size()));
    }

    @Test
    void testCreateEvent() {
        Event event = new Event();
        when(eventService.saveEvent(any(Event.class))).thenReturn(event);
        ResponseEntity<Event> response = eventController.createEvent(event);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(event, response.getBody());
    }

    @Test
    void testGetEventById_WhenEventExists() throws Exception {
        Long eventId = 1L;
        Event event = new Event(users.get(0), "Event 1", "Type 1", "Description 1", "Place 1", "image1.jpg",
                "2035-06-01T08:30", "Address 1", "Assistants 1");
        when(eventService.getEventById(eventId)).thenReturn(Optional.of(event));

        ResponseEntity<Event> response = eventController.getEventById(eventId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(event, response.getBody());
    }

    @Test
    void testGetEventById_WhenEventDoesNotExist() throws Exception {
        Long eventId = 1L;
        when(eventService.getEventById(eventId)).thenReturn(Optional.empty());

        ResponseEntity<Event> response = eventController.getEventById(eventId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateEvent_WhenEventExists() {
        Long eventId = 1L;
        Event existingEvent = events.get(0);
        Event updatedEvent = new Event(users.get(0), "Updated Event", "Updated Type", "Updated Description",
                "Updated Place", "Updated Image", "2035-06-01T08:30", "Updated Address",
                "Updated Assistants");

        when(eventService.getEventById(eventId)).thenReturn(Optional.of(existingEvent));
        when(eventService.saveEvent(any(Event.class))).thenReturn(updatedEvent);

        ResponseEntity<Event> response = eventController.updateEvent(eventId, updatedEvent);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedEvent, response.getBody());
    }

    @Test
    void testUpdateEvent_WhenEventDoesNotExist() {
        Long eventId = 1L;
        Event updatedEvent = new Event(users.get(0), "Updated Event", "Updated Type", "Updated Description",
                "Updated Place", "Updated Image", "2035-06-01T08:30", "Updated Address",
                "Updated Assistants");

        when(eventService.getEventById(eventId)).thenReturn(Optional.empty());

        ResponseEntity<Event> response = eventController.updateEvent(eventId, updatedEvent);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteEvent_WhenEventExists() {
        Long eventId = 1L;
        Event event = events.get(0);
        when(eventService.getEventById(eventId)).thenReturn(Optional.of(event));

        ResponseEntity<String> response = eventController.deleteEvent(eventId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Event deleted successfully", response.getBody());
    }

    @Test
    void testDeleteEvent_WhenEventDoesNotExist() {
        Long eventId = 1L;
        when(eventService.getEventById(eventId)).thenReturn(Optional.empty());

        ResponseEntity<String> response = eventController.deleteEvent(eventId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Event not found", response.getBody());
    }

}
