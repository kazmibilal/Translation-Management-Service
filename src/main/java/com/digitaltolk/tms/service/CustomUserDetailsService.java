package com.digitaltolk.tms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    // In-memory user store (replace with database in production)
    private final Map<String, String> users = new HashMap<>();

    public CustomUserDetailsService() {
        // Initialize with some test users
        // In production, load from database
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // For demo purposes, create a default user if not found
        if (!users.containsKey(username)) {
            if ("user".equals(username)) {
                users.put(username, passwordEncoder.encode("password"));
            } else {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
        }

        return new User(username, users.get(username), new ArrayList<>());
    }

    public void saveUser(String username, String password) {
        users.put(username, passwordEncoder.encode(password));
    }
}