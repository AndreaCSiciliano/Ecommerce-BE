package com.example.ecommerce.controllers;

import com.example.ecommerce.dto.UserDto;
import com.example.ecommerce.entities.User;
import com.example.ecommerce.security.utils.JwtTokenUtil;
import com.example.ecommerce.services.UserService;
import com.example.ecommerce.utils.PasswordUtils;
import com.example.ecommerce.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private static final String TOKEN_HEADER = "Authorization";

    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping
    public ResponseEntity<Response<UserDto>> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Creating user {}", userDto);
        User user = this.convertUserDtoToUser(userDto);
        User newUser = this.userService.createUser(user);
        UserDto newUserDto = convertUserToUserDto(newUser);
        Response<UserDto> response = new Response<>();
        response.setData(newUserDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
        log.info("Getting user {}", id);
        User user = this.userService.getUserById(id);
        UserDto userDto = convertUserToUserDto(user);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping()
    public ResponseEntity<Response<UserDto>> getUserByToken(HttpServletRequest request) {
        log.info("Getting user by token");
        Response<UserDto> response = new Response<>();
        Optional<String> token = Optional.ofNullable(request.getHeader(TOKEN_HEADER));
        if (token.isPresent() && token.get().startsWith(BEARER_PREFIX)) {
            token = Optional.of(token.get().substring(7));
        }
        if (!token.isPresent()) {
            response.getErrors().add("Token not found");
        } else if (!jwtTokenUtil.isTokenValid(token.get())) {
            response.getErrors().add("Token expired or invalid");
        }
        if (!response.getErrors().isEmpty()) {
            return ResponseEntity.badRequest().body(response);
        }
        Optional<User> user = this.userService.getUserByEmail(jwtTokenUtil.getUsernameFromToken(token.get()));
        UserDto userDto = convertUserToUserDto(user.get());
        response.setData(userDto);
        return ResponseEntity.ok(response);
    }

    private UserDto convertUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(Optional.of(user.getId()));
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setRole(user.getRole());
        userDto.setMainPaymentMethod(user.getMainPaymentMethod());
        userDto.setAddress(user.getAddress());
        userDto.setNumber(user.getNumber());
        userDto.setComplement(user.getComplement());
        return userDto;
    }

    private User convertUserDtoToUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setRole(userDto.getRole());
        user.setEmail(userDto.getEmail());
        user.setPassword(PasswordUtils.generateBCrypt(userDto.getPassword()));
        user.setMainPaymentMethod(userDto.getMainPaymentMethod());
        user.setAddress(userDto.getAddress());
        user.setNumber(userDto.getNumber());
        user.setComplement(userDto.getComplement());
        return user;
    }
}
