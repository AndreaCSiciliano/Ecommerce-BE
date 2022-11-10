package com.example.ecommerce.services.impl;

import com.example.ecommerce.entities.Product;
import com.example.ecommerce.enums.Category;
import com.example.ecommerce.repositories.ProductRepository;
import com.example.ecommerce.services.ProductService;
import com.example.ecommerce.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    ProductRepository productRepository;

    @Override
    public Product getProductById(Long id) {
        log.info("Getting product by id {}", id);
        return this.productRepository.getReferenceById(id);
    }

    @Override
    public Product createProduct(Product product) {
        return this.productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(Category category) {
        return this.productRepository.findByCategory(category);
    }
}
