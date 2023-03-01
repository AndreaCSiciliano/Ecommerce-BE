package com.example.ecommerce.services.impl;

import com.example.ecommerce.entities.User;
import com.example.ecommerce.repositories.UserRepository;
import com.example.ecommerce.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public User createUser(User user) {
        log.info("Saving user {}", user);
        return this.userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        User oldUser = userRepository.getReferenceById(user.getId());
        oldUser.setName(user.getName());
        oldUser.setEmail(user.getEmail());
        oldUser.setAddress(user.getAddress());
        oldUser.setNumber(user.getNumber());
        oldUser.setComplement(user.getComplement());
        oldUser.setMainPaymentMethod(user.getMainPaymentMethod());
        return this.userRepository.save(oldUser);
    }

    @Override
    public User getUserById(Long id) {
        log.info("Getting user by id {}", id);
        return this.userRepository.getReferenceById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }
}
