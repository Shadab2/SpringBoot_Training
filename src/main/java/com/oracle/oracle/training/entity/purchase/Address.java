package com.oracle.oracle.training.entity.purchase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String type;
    private String name;
    private String street;
    private String state;
    private String country;
    private String zip;
}
