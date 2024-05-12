package com.example.demo.controllers;
import com.example.demo.models.Event;
import com.example.demo.models.User;
import com.example.demo.services.UserService;
import com.example.demo.services.EventService;
import com.example.demo.utils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:5173/*", "https://edventure-six.vercel.app" })
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final EventService eventService;

    @Autowired
    public UserController(UserService userService, EventService eventService) {
        this.userService = userService;
        this.eventService = eventService;

    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public  ResponseEntity<Object> getUserById(@PathVariable Long id) {
        try {
            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(user.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @PostMapping
    public ResponseEntity<User> loginUser(@RequestBody User request) {
        try {
            Optional<User> existingUser = userService.getUserById(request.getId());

            if (!existingUser.isPresent()) {
                User newUser = new User(request.getId(), request.getNickname(), request.getName(),
                        request.getLastname(), request.getEmail(),
                        request.getAvatar(), request.getLoggedDate());
                newUser = userService.saveUser(newUser);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(newUser);
            } else {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(existingUser.get());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User request) {
        try {
            Optional<User> existingUser = userService.getUserById(id);
            if (existingUser.isPresent()) {
                User updatedUser = existingUser.get();
                Utils.updateUserFields(updatedUser, request);
                updatedUser = userService.saveUser(updatedUser);
                return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @PutMapping("/{userId}/updateFollowingEvent/{eventId}")
    public  ResponseEntity<User> addUserFollowingEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        try {
            Optional<User> optionalUser = userService.getUserById(userId);
            Optional<Event> optionalEvent = eventService.getEventById(eventId);

            if (optionalUser.isPresent() && optionalEvent.isPresent()) {
                User user = optionalUser.get();
                Event event = optionalEvent.get();

                List<Event> userEvents = user.getFollowingEvents();
                userEvents.add(event);
                user.setFollowingEvents(userEvents);
                Utils.updateFollowersHistory(event, event.getUsersFollowing().size() + 1);
                eventService.saveEvent(event);
                User userUpdated = userService.saveUser(user);

                return ResponseEntity.status(HttpStatus.OK).body(userUpdated);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{userId}/updateFollowingEvent/{eventId}")
    public  ResponseEntity<User> removeUserFollowingEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        try {
            Optional<User> optionalUser = userService.getUserById(userId);
            Optional<Event> optionalEvent = eventService.getEventById(eventId);

            if (optionalUser.isPresent() && optionalEvent.isPresent()) {
                User user = optionalUser.get();
                Event event = optionalEvent.get();

                List<Event> userEvents = user.getFollowingEvents();
                boolean removed = userEvents.removeIf(userEvent -> userEvent.getId().equals(eventId));

                if (removed) {
                    user.setFollowingEvents(userEvents);
                    Utils.updateFollowersHistory(event, event.getUsersFollowing().size() - 1);
                    eventService.saveEvent(event);

                    User userUpdated = userService.saveUser(user);
                    return ResponseEntity.status(HttpStatus.OK).body(userUpdated);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(null);
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
