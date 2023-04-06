package com.oracle.oracle.training.services.functional;

import com.oracle.oracle.training.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.oracle.oracle.training.constants.Constants.API_SECRET_KEY;
import static com.oracle.oracle.training.constants.Constants.TOKEN_VALIDITY_TIME;

public class AuthService {
    public static String generateJWTToken(User user) {
        long timestamp = System.currentTimeMillis();
        return Jwts.builder().signWith(SignatureAlgorithm.HS256, API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + TOKEN_VALIDITY_TIME))
                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                .compact();
    }

}
