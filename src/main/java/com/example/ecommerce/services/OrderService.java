package com.example.ecommerce.services;

import com.example.ecommerce.entities.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(Order order);

    List<Order> getOrdersByUserId(Long id);

    Order getOrderById(Long id);
}
