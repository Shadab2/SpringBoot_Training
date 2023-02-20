package com.oracle.oracle.training.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Long id;
    private String name;
    private String username;
    private String email;

    @Embedded
    private Address address;

    private String phone;
    private String website;
    @Embedded
    private Company company;

}
