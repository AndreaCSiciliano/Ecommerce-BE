package com.example.ecommerce.dto;

import com.example.ecommerce.enums.PaymentMethod;
import com.example.ecommerce.enums.Role;
import lombok.Data;

import java.util.Optional;

@Data
public class UserDto {

    private Optional<Long> id = Optional.empty();

    private String name;

    private String email;

    private String password;

    private PaymentMethod mainPaymentMethod;

    private Role role;

    private String address;

    private String complement;

    private String number;
}
