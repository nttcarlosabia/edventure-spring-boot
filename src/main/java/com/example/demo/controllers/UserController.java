package com.example.demo.controllers;

import com.example.demo.models.Event;
import com.example.demo.models.User;
import com.example.demo.repositories.EventRepository;
import com.example.demo.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity getUserById(@PathVariable Long id) {
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
    public ResponseEntity loginUser(@RequestBody User request) {
        Optional<User> existingUser = userRepository.findById(request.getId());

        try {
            if (!existingUser.isPresent()) {
                User newUser = new User(request.getId(), request.getNickname(), request.getName(), request.getEmail(),
                        request.getAvatar(), request.getLoggedDate());
                existingUser = Optional.of(userRepository.save(newUser));
            }

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(existingUser.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable Long id, @RequestBody User request) {
        try {
            Optional<User> existingUser = userRepository.findById(id);
            if (existingUser.isPresent()) {
                User updatedUser = existingUser.get();
                // Verifica y actualiza cada atributo si se proporciona en la solicitud
                if (request.getNickname() != null) {
                    updatedUser.setNickname(request.getNickname());
                }
                if (request.getName() != null) {
                    updatedUser.setName(request.getName());
                }
                if (request.getEmail() != null) {
                    updatedUser.setEmail(request.getEmail());
                }
                if (request.getAvatar() != null) {
                    updatedUser.setAvatar(request.getAvatar());
                }
                if (request.getLoggedDate() != null) {
                    updatedUser.setLoggedDate(request.getLoggedDate());
                }
                if (request.getUserEvents() != null) {
                    updatedUser.setUserEvents(request.getUserEvents());
                }
                if (request.getFollowingEvents() != null) {
                    updatedUser.setFollowingEvents(request.getFollowingEvents());
                }
                // Guardar el usuario actualizado en la base de datos
                updatedUser = userRepository.save(updatedUser);
                return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + id);
            }
        } catch (Exception e) {
            // Capturar y registrar la excepción
            e.printStackTrace(); // Registra la excepción en la consola de la aplicación
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        try {
            userRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @PutMapping("/{userId}/addEvent/{eventId}")
    public ResponseEntity addUserEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            Optional<Event> optionalEvent = eventRepository.findById(eventId);

            if (optionalUser.isPresent() && optionalEvent.isPresent()) {
                User user = optionalUser.get();
                Event event = optionalEvent.get();

                // Obtener la lista existente de eventos del usuario
                List<Event> userEvents = user.getFollowingEvents();

                // Agregar el evento a la lista existente de eventos del usuario
                userEvents.add(event);

                // Actualizar la lista de eventos del usuario
                user.setFollowingEvents(userEvents);

                userRepository.save(user);

                return ResponseEntity.status(HttpStatus.OK)
                        .body("Event added to user's events list successfully." + event.getName());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or event not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{userId}/removeEvent/{eventId}")
    public ResponseEntity removeUserEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        try {
            Optional<User> optionalUser = userRepository.findById(userId);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.getUserEvents().removeIf(event -> event.getId().equals(eventId));
                userRepository.save(user);

                return ResponseEntity.status(HttpStatus.OK).body("Event removed from user's events list successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
