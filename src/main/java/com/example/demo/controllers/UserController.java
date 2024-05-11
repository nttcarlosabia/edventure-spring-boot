package com.example.demo.controllers;
import com.example.demo.models.Event;
import com.example.demo.models.User;
import com.example.demo.repositories.EventRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.utils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:5173/*", "https://edventure-six.vercel.app" })
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Autowired
    public UserController(UserRepository userRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public  ResponseEntity<Object> getUserById(@PathVariable Long id) {
        try {
            Optional<User> user = userRepository.findById(id);
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
            Optional<User> existingUser = userRepository.findById(request.getId());

            if (!existingUser.isPresent()) {
                User newUser = new User(request.getId(), request.getNickname(), request.getName(),
                        request.getLastname(), request.getEmail(),
                        request.getAvatar(), request.getLoggedDate());
                newUser = userRepository.save(newUser);
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
            Optional<User> existingUser = userRepository.findById(id);
            if (existingUser.isPresent()) {
                User updatedUser = existingUser.get();
                updateUserFields(updatedUser, request);
                updatedUser = userRepository.save(updatedUser);
                return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    private void updateUserFields(User existingUser, User request) {
        if (request.getNickname() != null) {
            existingUser.setNickname(request.getNickname());
        }
        if (request.getName() != null) {
            existingUser.setName(request.getName());
        }
        if (request.getLastname() != null) {
            existingUser.setLastname(request.getLastname());
        }
        if (request.getShowEmail() != null) {
            existingUser.setShowEmail(request.getShowEmail());
        }
        if (request.getEmail() != null) {
            existingUser.setEmail(request.getEmail());
        }
        if (request.getAvatar() != null) {
            existingUser.setAvatar(request.getAvatar());
        }
        if (request.getLoggedDate() != null) {
            existingUser.setLoggedDate(request.getLoggedDate());
        }
    }
    

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        try {
            userRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @PutMapping("/{userId}/updateFollowingEvent/{eventId}")
    public  ResponseEntity<User> addUserFollowingEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            Optional<Event> optionalEvent = eventRepository.findById(eventId);

            if (optionalUser.isPresent() && optionalEvent.isPresent()) {
                User user = optionalUser.get();
                Event event = optionalEvent.get();

                List<Event> userEvents = user.getFollowingEvents();
                userEvents.add(event);
                user.setFollowingEvents(userEvents);
                updateFollowersHistory(event, event.getUsersFollowing().size() + 1);
                User userUpdated = userRepository.save(user);

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
            Optional<User> optionalUser = userRepository.findById(userId);
            Optional<Event> optionalEvent = eventRepository.findById(eventId);

            if (optionalUser.isPresent() && optionalEvent.isPresent()) {
                User user = optionalUser.get();
                Event event = optionalEvent.get();

                List<Event> userEvents = user.getFollowingEvents();
                boolean removed = userEvents.removeIf(userEvent -> userEvent.getId().equals(eventId));

                if (removed) {
                    user.setFollowingEvents(userEvents);
                    updateFollowersHistory(event, event.getUsersFollowing().size() - 1);

                    User userUpdated = userRepository.save(user);
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

    private void updateFollowersHistory(Event event, int followersCount) {
        Map<String, Integer> followersHistoryMap = event.getFollowersHistory();
        String currentDateAsString = Utils.getCurrentDateAsString();
        if (followersHistoryMap.containsKey(currentDateAsString)) {
            int existingFollowersCount = followersHistoryMap.get(currentDateAsString);
            int newFollowersCount = followersCount;
            if (existingFollowersCount > followersCount) {
                newFollowersCount = existingFollowersCount - 1;
            }
            followersHistoryMap.put(currentDateAsString, newFollowersCount);
            
        } else {
            followersHistoryMap.put(currentDateAsString, followersCount);
        }

        event.setFollowersHistory(followersHistoryMap);
        eventRepository.save(event);
    }

}
