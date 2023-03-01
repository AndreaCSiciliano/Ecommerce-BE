package com.example.ecommerce.entities;

import com.example.ecommerce.enums.Role;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@MappedSuperclass
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String email;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "last_update_date")
    private Date lastUpdateDate;

    private Role role;

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
}
