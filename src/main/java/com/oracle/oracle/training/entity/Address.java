package com.oracle.oracle.training.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table( name = "address_tbl")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column( nullable = false)
    private String street;

    @Column( nullable = false)
    private String city;
    @Column( nullable = false)
    private String state;
    @Column( nullable = false)
    private String country;

    private Date dateCreated;
}
