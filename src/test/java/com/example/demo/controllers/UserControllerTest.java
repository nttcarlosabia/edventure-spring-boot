    package com.example.demo.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.models.User;
import com.example.demo.services.EventService;
import com.example.demo.services.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private EventService eventService;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private List<User> users;

    @BeforeEach
    public void setUp() {
        users = new ArrayList<>();
        users.add(new User(1L, "user1", "John", "Doe", "john@example.com", "avatar1.jpg", new Date(System.currentTimeMillis())));
        users.add(new User(2L, "user2", "Jane", "Doe", "jane@example.com", "avatar2.jpg", new Date(System.currentTimeMillis())));
    }

    @Test
    void testGetUsers() {
        when(userService.getUsers()).thenReturn(users);

        List<User> result = userController.getUsers();

        assertEquals(users.size(), result.size());
    }

    @Test
    void testGetUserById_WhenUserExists() {
        Long userId = 1L;
        User user = users.get(0);
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        ResponseEntity<Object> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testGetUserById_WhenUserDoesNotExist() {
        Long userId = 3L;
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = userController.getUserById(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found with id: " + userId, response.getBody());
    }

    @Test
    void testLoginUser_NewUser() {
        User newUser = new User(3L, "user3", "Michael", "Smith", "michael@example.com", "avatar3.jpg", new Date(System.currentTimeMillis()));
        lenient().when(userService.getUserById(newUser.getId())).thenReturn(Optional.empty());
        lenient().when(userService.saveUser(newUser)).thenReturn(newUser);

        ResponseEntity<User> response = userController.loginUser(newUser);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    void testLoginUser_ExistingUser() {
        User existingUser = users.get(0);
        when(userService.getUserById(existingUser.getId())).thenReturn(Optional.of(existingUser));

        ResponseEntity<User> response = userController.loginUser(existingUser);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(existingUser, response.getBody());
    }

    @Test
    void testUpdateUser_WhenUserExists() {
        Long userId = 1L;
        User existingUser = users.get(0);
        User updatedUser = new User(null, "user1-updated", null, null, "john@example.com", "avatar1.jpg", new Date(System.currentTimeMillis()));
        when(userService.getUserById(userId)).thenReturn(Optional.of(existingUser));

        ResponseEntity<User> response = userController.updateUser(userId, updatedUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(existingUser.getNickname(), updatedUser.getNickname());
    }

    @Test
    void testUpdateUser_WhenUserDoesNotExist() {
        Long userId = 3L;
        User updatedUser = new User(null, "user3", "Michael", "Smith", "michael@example.com", "avatar3.jpg", new Date(System.currentTimeMillis()));
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.updateUser(userId, updatedUser);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
