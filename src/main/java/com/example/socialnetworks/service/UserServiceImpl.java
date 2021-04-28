package com.example.socialnetworks.service;

import com.example.socialnetworks.model.User;
import com.example.socialnetworks.repos.UserRepo;
import com.example.socialnetworks.utils.TimerStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TimerStatus timerStatus;


    @Override
    public void create(User user) {
        user.setLastStatus("Offline");
        user.setCurrentStatus("Offline");
        userRepo.save(user);
    }

    @Override
    public List<User> readAll() {
        return userRepo.findAll();
    }

    @Override
    public User read(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public boolean update(User user, Long id) {
        if (userRepo.existsById(id)) {
            User oldUser = userRepo.findById(id).orElse(null);
            if (oldUser == null) {
                return false;
            }
            user.setId(id);
            user.setLastStatus(oldUser.getLastStatus());
            user.setCurrentStatus(oldUser.getCurrentStatus());
            userRepo.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
            return true;
        }
        return false;
    }


    @Override
    public boolean updateStatus(Long userId, String status) {
        if (userRepo.existsById(userId)) {
            User user = userRepo.findById(userId).orElse(null);
            if (user == null) {
                return false;
            }
            if (status.equals("Online")) {
                timerStatus.executeTaskT(userId);
            }
            String pastStatus = user.getCurrentStatus();
            user.setLastStatus(pastStatus);
            user.setCurrentStatus(status);
            userRepo.save(user);
            return true;

        }
        return false;
    }
}
