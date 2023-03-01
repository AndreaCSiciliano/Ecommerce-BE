package com.example.ecommerce.entities;

import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.enums.PaymentMethod;
import com.example.ecommerce.enums.ShippingMethod;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<ProductInOrder> productsInOrder;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private ShippingMethod shippingMethod;

    @Column(name = "total_amount")
    private double totalAmount;

    @Column(name = "order_status")
    private OrderStatus status;

    @Column(name = "creation_date")
    private Date creationDate;

    @PrePersist
    public void prePersist(){
        final Date date = new Date();
        creationDate = date;
    }
}
