package com.oracle.oracle.training.entity.post;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "link_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer link_id;
    private String description;
    private String link;
}
