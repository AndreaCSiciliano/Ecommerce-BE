package com.example.ecommerce.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        System.out.println(authException.getMessage());
        if (authException.getMessage().equals("Bad credentials")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Your e-mail or password is not correct");
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Access denied. You have to be authenticated in order to access this URL");
        }
    }
}
