package com.oracle.oracle.training.model;

import com.oracle.oracle.training.model.Geo;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileAddress {
    private String street;
    private String suite;
    private String city;
    private String zipcode;
    @Embedded
    private Geo geo;
}
