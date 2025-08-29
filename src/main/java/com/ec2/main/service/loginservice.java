package com.ec2.main.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ec2.main.model.Users;


@Service
public class loginservice {
    public Users fetchUserDetails(Long userId) {
        // Replace with real DB/service call
        if (userId == 7) {
            return new Users("Bhavya", "bhavya@demo.com", "FACULTY", (long)7);
        } else if (userId == 2025) {
            return new Users("Admin", "admin@demo.com", "REGISTRAR", (long)2025);
        } else {
            return null;
        }
    }
    public boolean isAllowedUser(String userCategory, int userId) {
        List<Integer> allowedIds = Arrays.asList(3365, 1126, 6326, 2025, 3809, 2484, 11045);
        return userCategory.equals("REGISTRAR") || userCategory.equals("FACULTY") || allowedIds.contains(userId);
    }
    public boolean isPermittedMachine(int userId, String ip) {
        // Example: allow all for testing
        return true;
    }

    
}
