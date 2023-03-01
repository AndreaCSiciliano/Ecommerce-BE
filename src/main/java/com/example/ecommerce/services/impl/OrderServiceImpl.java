package com.example.ecommerce.services.impl;

import com.example.ecommerce.entities.Order;
import com.example.ecommerce.repositories.OrderRepository;
import com.example.ecommerce.services.OrderService;
import com.example.ecommerce.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    private static Logger log = LoggerFactory.getLogger(UserService.class);

    @Override
    public Order createOrder(Order order) {
        log.info("Creating the order");
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrdersByUserId(Long id) {
        return this.orderRepository.findByUserId(id);
    }

    @Override
    public Order getOrderById(Long id) {
        return this.orderRepository.getReferenceById(id);
    }
}
