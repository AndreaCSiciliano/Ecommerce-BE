package com.example.ecommerce.security.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtAuthenticationDto {

    private String email;

    private String password;

    private String token;

    @Override
    public String toString() {
        return "JwtAuthenticationRequestDto [email=" + email + ", password=" + password + ", token=" + token + "]";
    }
}
