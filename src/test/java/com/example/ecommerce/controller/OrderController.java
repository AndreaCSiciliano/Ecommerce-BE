package com.example.ecommerce.controller;

import com.example.ecommerce.entities.Order;
import com.example.ecommerce.entities.ProductInOrder;
import com.example.ecommerce.entities.User;
import com.example.ecommerce.enums.PaymentMethod;
import com.example.ecommerce.enums.ShippingMethod;
import com.example.ecommerce.services.OrderService;
import com.example.ecommerce.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserService userService;

    private static final String BASE_URL = "/api/order/";
    private static final Date DATE = new Date();
    private static final Long ID = 1L;
    private static final List<ProductInOrder> PRODUCT_IN_ORDER_LIST = new ArrayList<>();
    private static final User USER = new User();
    private static final PaymentMethod PAYMENT_METHOD = PaymentMethod.PAYPAL;
    private static final ShippingMethod SHIPPING_METHOD = ShippingMethod.FASTEST;
    private static final Date ORDER_DATE = new Date();

    @Test
    public void createOrder() throws Exception {
        Order order = getOrderData();
        BDDMockito.given(this.userService.getUserById(Mockito.anyLong())).willReturn(new User());
        BDDMockito.given(this.orderService.createOrder(Mockito.any(Order.class))).willReturn(order);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .content(this.getRequestJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private String getRequestJson() throws JsonProcessingException {
        Order order = new Order();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(order);
    }

    private Order getOrderData() {
        Order order = new Order();
        order.setUser(USER);
        order.getUser().setId(1L);
        order.setPaymentMethod(PAYMENT_METHOD);
        order.setShippingMethod(SHIPPING_METHOD);
//       order.setOrderItem(ORDER_ITEMS);
        return order;
    }
}
