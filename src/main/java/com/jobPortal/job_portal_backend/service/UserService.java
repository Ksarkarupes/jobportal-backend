package com.jobPortal.job_portal_backend.service;

import com.jobPortal.job_portal_backend.models.User;
import com.jobPortal.job_portal_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public void addUser(User user){
        String email = user.getEmail();
        email = email.toLowerCase();
        user.setEmail(email);
        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
        if(userOptional.isPresent()) throw new RuntimeException("Email already registered. Please login.");
        userRepository.save(user);
    }
    public User getUser(User loginRequest) {
        String email = loginRequest.getEmail().toLowerCase();
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (loginRequest.getPassword().equals(user.getPassword())
                    && loginRequest.getUserType().equals(user.getUserType())) {
                return user;
            } else {
                throw new RuntimeException("Incorrect password or user type");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public User findById(int id){
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);
    }
}
