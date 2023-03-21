package com.oracle.oracle.training.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(
        name = "user_profile_tbl",
        uniqueConstraints = @UniqueConstraint(
                name = "username_unique",
                columnNames = "username"
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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "pc_fid", referencedColumnName = "id")
    private List<Company> companies = new ArrayList<>();
}
