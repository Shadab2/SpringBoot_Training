package com.oracle.oracle.training.services;

import com.oracle.oracle.training.consumingwebservice.rest.FetchUserClient;
import com.oracle.oracle.training.entity.*;
import com.oracle.oracle.training.entity.purchase.Item;
import com.oracle.oracle.training.entity.purchase.PurchaseOrder;
import com.oracle.oracle.training.exceptions.BadRequestException;
import com.oracle.oracle.training.exceptions.RequiredFieldsMissinigException;
import com.oracle.oracle.training.exceptions.ResourceNotFound;
import com.oracle.oracle.training.model.Geo;
import com.oracle.oracle.training.model.UserProfileAddress;
import com.oracle.oracle.training.repository.CompanyRepository;
import com.oracle.oracle.training.repository.UserProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class UserProfileServiceImp implements UserProfileService{
    @Autowired
    UserProfileRepository userProfileRepository;
    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    FetchUserClient fetchUserClient;

    @Autowired
    JSONParseService jsonParseService;
    @Autowired
    XMLParseService xmlParseService;


    private UserProfile convertParsedJSONToUserProfile(Map<String,Object> parsedUserProfile){
        try{
            UserProfile userProfile = new UserProfile();
            if(!parsedUserProfile.containsKey("id")) {
                throw new RuntimeException();
            }
            userProfile.setId((Integer) parsedUserProfile.get("id"));
            userProfile.setName((String)parsedUserProfile.get("name"));
            userProfile.setUsername((String)parsedUserProfile.get("username"));
            userProfile.setEmail((String)parsedUserProfile.get("email"));
            userProfile.setPhone((String)parsedUserProfile.get("phone"));
            userProfile.setWebsite((String)parsedUserProfile.get("website"));

            Map<String,Object> parsedAddress = (Map)parsedUserProfile.get("address");
            if(parsedAddress!=null) {
                UserProfileAddress address1 = new UserProfileAddress();
                address1.setCity((String) parsedAddress.get("city"));
                address1.setZipcode((String) parsedAddress.get("zipcode"));
                address1.setStreet((String) parsedAddress.get("street"));
                address1.setSuite((String) parsedAddress.get("suite"));

                Geo geo = new Geo();
                Map<String, Object> parsedGeo = (Map) parsedAddress.get("geo");
                if(parsedGeo!=null) {
                    geo.setLat((String) parsedGeo.get("lat"));
                    geo.setLng((String) parsedGeo.get("lng"));
                }
                address1.setGeo(geo);
                userProfile.setAddress(address1);
            }

            Map<String,Object> parsedCompany = (Map)parsedUserProfile.get("company");
            if(parsedCompany!=null) {
                Company company1 = new Company();
                company1.setName((String) parsedCompany.get("name"));
                company1.setBs((String) parsedCompany.get("bs"));
                company1.setCatchPhrase((String) parsedCompany.get("catchPhrase"));
                userProfile.getCompanies().add(company1);
            }
            return userProfile;
        }catch (Exception e){
            throw new RequiredFieldsMissinigException("Required Fields Missing!");
        }
    }

    @Override
    public List<UserProfile> saveIfNotExistsAndfetchAllUsers() {
        try {
        String  responseJSON = fetchUserClient.fetchAllUsers();
        List<Object> parsedJSON = jsonParseService.parseJSONArray(responseJSON);
        List<UserProfile> userProfiles =  new ArrayList<>();
        for(Object parsedProperty:parsedJSON){
            userProfiles.add(convertParsedJSONToUserProfile((Map)parsedProperty));
        }
        for(UserProfile u:userProfiles){
            if(userProfileRepository.existsById(u.getId())) continue;
            userProfileRepository.save(u);
        }
        return userProfileRepository.findAll();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    @Override
    public List<PurchaseOrder> getPurchaseOrders() {
        try{
        File file = new File("C:\\Users\\shada\\IdeaProjects\\oracle_training\\src\\main\\resources\\xml\\PurhchaseOrder.xml");
        List<PurchaseOrder> list = (List) xmlParseService.parseXML(file).get("PurchaseOrder");
        List<PurchaseOrder> purchaseOrders = new ArrayList<>();
        for(Object purchaseOrder:list){
            purchaseOrders.add(convertParsedXMltoPurchaseOrder(purchaseOrder));
        }
        return  purchaseOrders;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ByteArrayInputStream getUserDownloadInfo(Integer id){
        Optional<UserProfile> userProfile = userProfileRepository.findById(id);
        if(userProfile.isPresent()){
            try {
                return ExcelService.convertUserProfileToExcel(userProfile.get());
            } catch (IOException e) {
                throw new BadRequestException("Bad request");
            }
        }
        throw new ResourceNotFound("No such user");
    }

    @Override
    public UserProfile getUser(Integer id) {
       Optional<UserProfile> userProfileOptional = userProfileRepository.findById(id);
       if(userProfileOptional.isPresent()) return userProfileOptional.get();
       throw new ResourceNotFound("No such userProfile");
    }
    public List<Company> getAllCompanies(){
        return companyRepository.findAll();
    }

    private PurchaseOrder convertParsedXMltoPurchaseOrder(Object purchaseOrderMap) {
        try {
        Map<String,Object> map = (Map)purchaseOrderMap;
        PurchaseOrder purchaseOrder = new PurchaseOrder();

        purchaseOrder.setPurchaseOrderNumber(Long.parseLong((String)map.get("PurchaseOrderNumber")));
        if(map.containsKey("OrderDate")){
        Date orderDate = new SimpleDateFormat("yyyy-mm-dd").parse((String)map.get("OrderDate"));
        purchaseOrder.setOrderDate(orderDate);
        }
        purchaseOrder.setDeliveryNotes((String)map.get("DeliveryNotes"));

        List<Object> addressList = (List)map.get("Address");
        List<com.oracle.oracle.training.entity.purchase.Address> addresses = purchaseOrder.getAddresses();
        for(int i = 0; i<addressList.size();i++){
            Map<String,Object> addressMap = (Map)addressList.get(i);
            com.oracle.oracle.training.entity.purchase.Address address = new com.oracle.oracle.training.entity.purchase.Address();
            address.setCountry((String)addressMap.get("Country"));
            address.setName((String)addressMap.get("Name"));
            address.setZip((String)addressMap.get("Zip"));
            address.setState((String)addressMap.get("State"));
            address.setStreet((String)addressMap.get("Street"));
            address.setType((String)addressMap.get("Type"));
            addresses.add(address);
        }

        List<Item> items = purchaseOrder.getItems();
        Map<String,Object> itemsMap = (Map)map.get("Items");

        if(itemsMap.get("Item") instanceof List) {
            List<Object> itemList = (List) itemsMap.get("Item");
            for (int i = 0; i < itemList.size(); i++) {
                Map<String, Object> itemMap = (Map) itemList.get(i);
                addItemToPurchaseItems(items,itemMap);
            }
        }else if(itemsMap.get("Item") instanceof Map) {
            Map<String,Object> itemMap = (Map)itemsMap.get("Item");
            addItemToPurchaseItems(items,itemMap);
        }
        return  purchaseOrder;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void addItemToPurchaseItems( List<Item> items, Map<String, Object> itemMap) throws ParseException {
        Item item = new Item();
        item.setProductName((String) itemMap.get("ProductName"));
        item.setQuantity(Integer.parseInt((String) itemMap.getOrDefault("Quantity","0")));
        item.setPartNumber((String) itemMap.get("PartNumber"));
        item.setUsPrice(Double.parseDouble((String) itemMap.getOrDefault("USPrice","0")));
        if(itemMap.containsKey("ShipDate")){
        Date shipDate = new SimpleDateFormat("yyyy-mm-dd").parse((String) itemMap.get("ShipDate"));
        item.setShipDate(shipDate);
        }
        items.add(item);
    }
}
