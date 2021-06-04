package com.example.socialnetworks.controller;

import com.example.socialnetworks.exception.ApiRequestException;
import com.example.socialnetworks.exception.UserNotFoundRequestException;
import com.example.socialnetworks.model.User;
import com.example.socialnetworks.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    private static Logger logger = Logger.getLogger(UserController.class.getName());

    @GetMapping("/users")
    public ResponseEntity<List<User>> readAll() {
        List<User> userList = this.userService.readAll();
        if (userList.isEmpty()) {
            throw new UserNotFoundRequestException("User list is empty");
        }
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<Long> create(@RequestBody User user) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (user == null) {
            throw new ApiRequestException("User is null");
        }
        userService.create(user);
        logger.info("Create user");
        return new ResponseEntity<Long>(user.getId(), HttpStatus.CREATED);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> read(@PathVariable("id") Long id) {
        if (id == null) {
            throw new ApiRequestException("Id is null");
        }
        User user = this.userService.read(id);
        if (user == null) {
            throw new UserNotFoundRequestException("Not found user");
        }
        logger.info("Read user");
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> put(@PathVariable("id") Long id, @RequestBody User user) {
        if (user == null || id == null) {
            throw new ApiRequestException("User or id is null");
        }
        this.userService.update(user, id);
        logger.info("Update user");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<User> delete(@PathVariable("id") Long id) {
        if (id == null) {
            throw new ApiRequestException("Id is null");
        }
        this.userService.delete(id);
        logger.info("Delete user");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/users/{id}/status/{status}")
    public ResponseEntity<String> updateStatus(@PathVariable("id") Long userId,
                                               @PathVariable("status") String status) {
        if (userId == null || status == null) {
            throw new ApiRequestException("userId  or status is null");
        }
        this.userService.updateStatus(userId, status);
        logger.info("Update user status");
        return new ResponseEntity<>(userId + " " +
                this.userService.read(userId).getCurrentStatus() + " " +
                this.userService.read(userId).getLastStatus(), HttpStatus.OK);
    }
}
