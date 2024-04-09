package com.example.demo.controllers;

import com.example.demo.models.LoginRequest;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.utils.Utils;

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
    public User loginUser(@RequestBody LoginRequest loginRequest) {
        Long formatId = Utils.formatId(loginRequest.getSub());

        Optional<User> existingUser = userRepository.findById(formatId);

        if (!existingUser.isPresent()) {
            User newUser = new User(formatId, loginRequest.getNickname(), loginRequest.getName(),
                    loginRequest.getEmail(), loginRequest.getPicture(), loginRequest.getUpdated_at());
            existingUser = Optional.of(userRepository.save(newUser));
        }

        return existingUser.get();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
