package com.oracle.oracle.training.services;

import com.oracle.oracle.training.consumingwebservice.rest.FetchUserClient;
import com.oracle.oracle.training.entity.Address;
import com.oracle.oracle.training.entity.Company;
import com.oracle.oracle.training.entity.Geo;
import com.oracle.oracle.training.entity.UserProfile;
import com.oracle.oracle.training.repository.UserProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserProfileServiceImp implements UserProfileService{
    @Autowired
    UserProfileRepository userProfileRepository;
    @Autowired
    FetchUserClient fetchUserClient;

    @Autowired
    JSONParseService jsonParseService;

    private UserProfile convertParsedJSONToUserProfile(Map<String,Object> parsedUserProfile){
        UserProfile userProfile = new UserProfile();
        userProfile.setId((Integer) parsedUserProfile.get("id"));
        userProfile.setName((String)parsedUserProfile.get("name"));
        userProfile.setUsername((String)parsedUserProfile.get("username"));
        userProfile.setEmail((String)parsedUserProfile.get("email"));
        userProfile.setPhone((String)parsedUserProfile.get("phone"));
        userProfile.setWebsite((String)parsedUserProfile.get("website"));
        Map<String,Object> parsedAddress = (Map)parsedUserProfile.get("address");

        Address address1 = new Address();
        address1.setCity((String)parsedAddress.get("city"));
        address1.setZipcode((String)parsedAddress.get("zipcode"));
        address1.setStreet((String)parsedAddress.get("street"));
        address1.setSuite((String)parsedAddress.get("suite"));

        Geo geo = new Geo();
        Map<String,Object> parsedGeo = (Map) parsedAddress.get("geo");
        geo.setLat((String)parsedGeo.get("lat"));
        geo.setLng((String)parsedGeo.get("lng"));

        address1.setGeo(geo);
        userProfile.setAddress(address1);

        Map<String,Object> parsedCompany = (Map)parsedUserProfile.get("company");
        Company company1 = new Company();
        company1.setName((String)parsedCompany.get("name"));
        company1.setBs((String)parsedCompany.get("bs"));
        company1.setCatchPhrase((String)parsedCompany.get("catchPhrase"));

        userProfile.setCompany(company1);
        return userProfile;
    }

    @Override
    public List<UserProfile> saveIfNotExistsAndfetchAllUsers() {
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
    }
}
