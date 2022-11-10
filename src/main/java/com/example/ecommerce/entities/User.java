package com.example.ecommerce.entities;

import com.example.ecommerce.enums.PaymentMethod;
import com.example.ecommerce.enums.Role;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "password", nullable = false)
    @Size(min = 4, message = "at least 4 characters")
    private String password;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "last_update_date")
    private Date lastUpdateDate;

    @PreUpdate
    public void preUpdate() {
        lastUpdateDate = new Date();
    }

    @PrePersist
    public void prePersist() {
        final Date date = new Date();
        lastUpdateDate = date;
        creationDate = date;
    }

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "main_payment_method")
    private PaymentMethod mainPaymentMethod;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order> orders;

    @Column(name = "address", nullable = true, length = 100)
    private String address;

    @Column(name = "complement", nullable = true)
    private String complement;

    @Column(name = "number", nullable = true, length = 50)
    private String number;
}
