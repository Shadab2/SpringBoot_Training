package com.oracle.oracle.training.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(
        name = "user_profile_tbl",
        uniqueConstraints = @UniqueConstraint(
                name = "id",
                columnNames = "id"
        )
)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserProfile {

    @Id
    private Integer id;
    private String name;
    private String username;
    private String email;


    private Address address;
    private String phone;
    private String website;

    @ManyToMany
    @JoinTable(
            name = "User_Profile_Companies",
            joinColumns = { @JoinColumn(name = "id") },
            inverseJoinColumns = { @JoinColumn(name = "company_id") }
    )
    private Set<Company> companies = new HashSet<>();
}
