package com.example.ecommerce.services.impl;

import com.example.ecommerce.entities.ProductInOrder;
import com.example.ecommerce.repositories.ProductInOrderRepository;
import com.example.ecommerce.services.ProductInOrderService;
import com.example.ecommerce.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductInOrderServiceImpl implements ProductInOrderService {

    private static Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    ProductInOrderRepository productInOrderRepository;

    @Override
    public ProductInOrder createProductInOrder(ProductInOrder productInOrder) {
        log.info("Saving a product in order");
        return productInOrderRepository.save(productInOrder);
    }
}
