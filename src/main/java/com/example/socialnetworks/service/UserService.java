package com.example.socialnetworks.service;

import com.example.socialnetworks.model.User;

import java.util.List;

public interface UserService {

    void create(User user);

    List<User> readAll();

    User read(Long id);

    boolean update(User user, Long id);

    boolean delete(Long id);

    boolean updateStatus(Long userId, String status);
}
