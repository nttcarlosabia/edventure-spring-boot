package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/{nickname}")
    public User getUserById(@PathVariable String nickname) {
        return userRepository.findById(nickname)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + nickname));
    }

    @PutMapping("/{nickname}")
    public User updateUser(@PathVariable String nickname, @RequestBody User user) {
        user.setNickname(nickname); // asegura que el ID se establezca correctamente
        return userRepository.save(user);
    }

    @DeleteMapping("/{nickname}")
    public void deleteUser(@PathVariable String nickname) {
        userRepository.deleteById(nickname);
    }
}
