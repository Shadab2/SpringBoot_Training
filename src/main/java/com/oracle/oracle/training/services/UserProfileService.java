package com.oracle.oracle.training.services;

import com.oracle.oracle.training.entity.Company;
import com.oracle.oracle.training.entity.UserProfile;
import com.oracle.oracle.training.entity.purchase.PurchaseOrder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface UserProfileService {
    List<UserProfile> saveIfNotExistsAndfetchAllUsers();

    List<PurchaseOrder> getPurchaseOrders();

    ByteArrayInputStream getUserDownloadInfo(Integer id) throws IOException;

    UserProfile getUser(Integer id);

     List<Company> getAllCompanies();
}
