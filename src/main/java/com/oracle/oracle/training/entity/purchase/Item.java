package com.oracle.oracle.training.entity.purchase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Item {
    private String partNumber;
    private String productName;
    private Integer quantity;
    private Double usPrice;
    private Date shipDate;
}
