package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public User loginUser(@RequestBody User request) {
        Optional<User> existingUser = userRepository.findById(request.getId());

        if (!existingUser.isPresent()) {
            User newUser = new User(request.getId(), request.getNickname(), request.getName(), request.getEmail(),
                    request.getAvatar(), request.getLoggedDate());
            existingUser = Optional.of(userRepository.save(newUser));
        }

        return existingUser.get();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
