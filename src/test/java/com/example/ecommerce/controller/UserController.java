package com.example.ecommerce.controller;

import com.example.ecommerce.entities.User;
import com.example.ecommerce.enums.PaymentMethod;
import com.example.ecommerce.enums.Role;
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

import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class UserController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private static final String BASE_URL = "/api/customer/";
    private static final Date DATE = new Date();
    private static final Long ID = 1L;
    private static final String NAME = "test user";
    private static final PaymentMethod PAYMENT_METHOD = PaymentMethod.PAYPAL;
    private static final String EMAIL = "user@test.com";

    @Test
    public void createCustomer() throws Exception {
        User user = getUserData();
        BDDMockito.given(this.userService.createUser(Mockito.any(User.class))).willReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .content(this.getRequestJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private String getRequestJson() throws JsonProcessingException {
        User user = new User();
        user.setId(null);
        user.setEmail(EMAIL);
        user.setMainPaymentMethod(PAYMENT_METHOD);
        user.setName(NAME);
        user.setRole(Role.ROLE_USER);
        user.setCreationDate(DATE);
        user.setLastUpdateDate(DATE);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(user);
    }

    private User getUserData() {
        User user = new User();
        user.setId(ID);
        user.setEmail(EMAIL);
        user.setMainPaymentMethod(PaymentMethod.PAYPAL);
        user.setName(NAME);
        user.setRole(Role.ROLE_USER);
        user.setCreationDate(DATE);
        user.setLastUpdateDate(DATE);
        return user;
    }
}
