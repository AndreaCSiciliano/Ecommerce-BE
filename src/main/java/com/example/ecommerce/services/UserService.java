package com.example.ecommerce.services;

import com.example.ecommerce.entities.User;

import java.util.Optional;

public interface UserService {

    User createUser(User user);

    User updateUser(User user);

    User getUserById(Long id);

    Optional<User> getUserByEmail(String email);
}
