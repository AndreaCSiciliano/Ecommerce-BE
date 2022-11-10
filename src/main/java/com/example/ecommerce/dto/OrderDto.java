package com.example.ecommerce.dto;

import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.enums.PaymentMethod;
import com.example.ecommerce.enums.ShippingMethod;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Data
public class OrderDto {

    private Optional<Long> id = Optional.empty();

    private Long userId;

    private List<ProductInOrderDto> productsInOrder;

    private PaymentMethod paymentMethod;

    private ShippingMethod shippingMethod;

    private OrderStatus status;

    private double totalAmount;

    private Optional<Date> creationDate = Optional.empty();
}
