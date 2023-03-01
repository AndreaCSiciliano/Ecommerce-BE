package com.example.ecommerce.controllers;

import com.example.ecommerce.entities.Product;
import com.example.ecommerce.enums.Category;
import com.example.ecommerce.services.ProductService;
import com.example.ecommerce.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/product")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
        log.info("Getting product {}", id);
        Product product = this.productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/")
    public ResponseEntity<List<Product>> getProducts() {
        log.info("Getting all products");
        List<Product> products = this.productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductByCategory(@PathVariable("category") Category category) {
        log.info("Getting products by category {}", category);
        List<Product> products = this.productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        log.info("Creating product {}", product);
        this.productService.createProduct(product);
        return ResponseEntity.ok(product);
    }

}
