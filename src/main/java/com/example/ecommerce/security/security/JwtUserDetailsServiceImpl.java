package com.example.ecommerce.security.security;

import com.example.ecommerce.entities.User;
import com.example.ecommerce.security.JwtUserFactory;
import com.example.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userService.getUserByEmail(username);

        if (user.isPresent()) {
            return JwtUserFactory.create(user.get());
        }
        throw new UsernameNotFoundException("No user found with this email");
    }
}
