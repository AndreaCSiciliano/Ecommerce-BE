package com.example.ecommerce.dto;

import lombok.Data;

import java.util.Optional;

@Data
public class ProductInOrderDto {

    private Optional<Long> id = Optional.empty();

    private Long productId;

    private Integer quantity;
}
