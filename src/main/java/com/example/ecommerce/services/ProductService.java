package com.example.ecommerce.services;

import com.example.ecommerce.entities.Product;
import com.example.ecommerce.enums.Category;

import java.util.List;

public interface ProductService {

    Product getProductById(Long id);

    Product createProduct(Product product);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(Category category);

}
