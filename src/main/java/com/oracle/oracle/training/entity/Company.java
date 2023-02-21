package com.oracle.oracle.training.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "company_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(
                name = "name",
                column = @Column(name = "company_name")
        )}
)
public class Company {
    private String name;
    private String catchPhrase;
    private String  bs;
}
