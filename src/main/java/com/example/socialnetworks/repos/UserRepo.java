package com.example.socialnetworks.repos;

import com.example.socialnetworks.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}
