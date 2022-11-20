package com.example.ecommerce.controllers;

import com.example.ecommerce.dto.OrderDto;
import com.example.ecommerce.dto.ProductInOrderDto;
import com.example.ecommerce.entities.Order;
import com.example.ecommerce.entities.Product;
import com.example.ecommerce.entities.ProductInOrder;
import com.example.ecommerce.entities.User;
import com.example.ecommerce.security.utils.JwtTokenUtil;
import com.example.ecommerce.services.OrderService;
import com.example.ecommerce.services.ProductInOrderService;
import com.example.ecommerce.services.ProductService;
import com.example.ecommerce.services.UserService;
import com.example.ecommerce.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/order")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private static final String TOKEN_HEADER = "Authorization";

    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    ProductInOrderService productInOrderService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/orders")
    public ResponseEntity<Response<List<OrderDto>>> getOrdersByUserId(HttpServletRequest request) {
        Response<List<OrderDto>> response = new Response<>();
        Optional<String> token = Optional.ofNullable(request.getHeader(TOKEN_HEADER));
        if (token.isPresent() && token.get().startsWith(BEARER_PREFIX)) {
            token = Optional.of(token.get().substring(7));
        }
        if (!token.isPresent()) {
            response.getErrors().add("Token not found.");
        } else if (!jwtTokenUtil.isTokenValid(token.get())) {
            response.getErrors().add("Token expired or invalid.");
        }
        if (!response.getErrors().isEmpty()) {
            return ResponseEntity.badRequest().body(response);
        }
        Optional<User> user = this.userService.getUserByEmail(jwtTokenUtil.getUsernameFromToken(token.get()));
        List<Order> orders = this.orderService.getOrdersByUserId(user.get().getId());
        List<OrderDto> orderDtos = new ArrayList<>();
        orders.forEach(order -> orderDtos.add(convertOrderToOrderDto(order)));
        response.setData(orderDtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<OrderDto>> getOrderById(@PathVariable("id") Long id) {
        Response<OrderDto> response = new Response<>();
        Order order = this.orderService.getOrderById(id);
        OrderDto orderDto = convertOrderToOrderDto(order);
        response.setData(orderDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto orderDto) {
        log.info("Creating order {}", orderDto);
        Order order = convertOrderDtoToOrder(orderDto);
        order.setTotalAmount(calculateTotalAmount(orderDto));
        Order createdOrder = this.orderService.createOrder(order);
        OrderDto createdOrderDto = convertOrderToOrderDto(createdOrder);
        return ResponseEntity.ok(createdOrderDto); //quantity --
    }

    private double calculateTotalAmount(OrderDto orderDto) {
        AtomicReference<Double> totalAmount = new AtomicReference<>((double) 0);
        orderDto.getProductsInOrder().forEach(productInOrderDto -> {
            Product product = productService.getProductById(productInOrderDto.getProductId());
            totalAmount.set(totalAmount.get() + (product.getPrice() * productInOrderDto.getQuantity()));
        });
        return totalAmount.get();
    }

    private OrderDto convertOrderToOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(Optional.of(order.getId()));
        orderDto.setUserId(order.getUser().getId());
        orderDto.setProductsInOrder(convertProductsInOrderToProductsInOrderDto(order.getProductsInOrder()));
        orderDto.setPaymentMethod(order.getPaymentMethod());
        orderDto.setShippingMethod(order.getShippingMethod());
        orderDto.setStatus(order.getStatus());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setCreationDate(Optional.of(order.getCreationDate()));
        return orderDto;
    }

    private List<ProductInOrderDto> convertProductsInOrderToProductsInOrderDto(List<ProductInOrder> productsInOrder) {
        List<ProductInOrderDto> productInOrderDtos = new ArrayList<>();
        productsInOrder.forEach(productInOrder -> {
            ProductInOrderDto productInOrderDto = new ProductInOrderDto();
            productInOrderDto.setId(Optional.of(productInOrder.getId()));
            productInOrderDto.setProductId(productInOrder.getProduct().getId());
            productInOrderDto.setQuantity(productInOrder.getQuantity());
            productInOrderDtos.add(productInOrderDto);
        });
        return productInOrderDtos;
    }

    private Order convertOrderDtoToOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setUser(userService.getUserById(orderDto.getUserId()));
        order.setShippingMethod(orderDto.getShippingMethod());
        order.setPaymentMethod(orderDto.getPaymentMethod());
        order.setStatus((orderDto.getStatus()));
        order.setProductsInOrder(convertProductsInOrderDtoToProductsInOrder(orderDto.getProductsInOrder(), order));
        return order;
    }

    private List<ProductInOrder> convertProductsInOrderDtoToProductsInOrder(List<ProductInOrderDto> productInOrderDtos, Order order) {
        List<ProductInOrder> productsInOrder = new ArrayList<>();
        productInOrderDtos.forEach(productInOrderDto -> {
            ProductInOrder productInOrder = new ProductInOrder();
            productInOrder.setProduct(productService.getProductById(productInOrderDto.getProductId()));
            productInOrder.setQuantity(productInOrderDto.getQuantity());
            productInOrder.setOrder(order);
            productsInOrder.add(productInOrder);
        });
        return productsInOrder;
    }
}
