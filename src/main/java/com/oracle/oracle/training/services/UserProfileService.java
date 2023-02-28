package com.oracle.oracle.training.services;

import com.oracle.oracle.training.entity.UserProfile;
import com.oracle.oracle.training.entity.purchase.PurchaseOrder;

import java.util.List;

public interface UserProfileService {
    List<UserProfile> saveIfNotExistsAndfetchAllUsers();

    List<PurchaseOrder> getPurchaseOrders();
}
