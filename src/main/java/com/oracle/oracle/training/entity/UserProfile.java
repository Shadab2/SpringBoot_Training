package com.oracle.oracle.training.entity;

import com.oracle.oracle.training.model.UserProfileAddress;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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


    @Embedded
    private UserProfileAddress address;
    private String phone;
    private String website;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "pc_fid", referencedColumnName = "id")
    private List<Company> companies = new ArrayList<>();
}
