package com.oracle.oracle.training.entity.purchase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class PurchaseOrder {
    private  long purchaseOrderNumber;
    private Date orderDate;
    private List<Address> addresses = new ArrayList<>();
    private String deliveryNotes;
    private List<Item> items = new ArrayList<>();

}
