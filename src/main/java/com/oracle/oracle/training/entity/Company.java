package com.oracle.oracle.training.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name = "company_tbl", uniqueConstraints = @UniqueConstraint(
        name = "unique_name",
        columnNames = "name"
))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Column(name = "company_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer companyId;
    private String name;
    private String catchPhrase;
    private String  bs;

}
