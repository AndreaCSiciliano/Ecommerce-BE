package com.example.ecommerce.security.controller;

import com.example.ecommerce.dto.UserDto;
import com.example.ecommerce.entities.User;
import com.example.ecommerce.security.dto.JwtAuthenticationDto;
import com.example.ecommerce.security.utils.JwtTokenUtil;
import com.example.ecommerce.services.UserService;
import com.example.ecommerce.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    private static final String TOKEN_HEADER = "Authorization";

    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping
    public ResponseEntity<Response<JwtAuthenticationDto>> generateTokenJwt(
            @Valid @RequestBody JwtAuthenticationDto authenticationDto, BindingResult result)
        throws AuthenticationException { //Generates and returns a new JWT Token.
        Response<JwtAuthenticationDto> response = new Response<>();
        if (result.hasErrors()) {
            log.info("Error found: {}", result.getAllErrors());
            result.getAllErrors()
                    .forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }
        log.info("Generating token for e-mail {}.", authenticationDto.getEmail());
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(authenticationDto.getEmail(), authenticationDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationDto.getEmail());
        String token = jwtTokenUtil.getToken(userDetails);
        JwtAuthenticationDto user = new JwtAuthenticationDto();
        user.setEmail(jwtTokenUtil.getUsernameFromToken(token));
        user.setToken(token);
        response.setData(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh") //Generates a new token with a new expiration time.
    public ResponseEntity<Response<JwtAuthenticationDto>> generateRefreshToken(HttpServletRequest request) {
        log.info("Generating refresh JWT");
        Response<JwtAuthenticationDto> response = new Response<>();
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
        JwtAuthenticationDto user = new JwtAuthenticationDto();
        user.setEmail(jwtTokenUtil.getUsernameFromToken(token.get()));
        user.setToken(jwtTokenUtil.refreshToken(token.get()));
        response.setData(user);
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
}
