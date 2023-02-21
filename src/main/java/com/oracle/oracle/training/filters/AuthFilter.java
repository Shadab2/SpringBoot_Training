package com.oracle.oracle.training.filters;

import com.oracle.oracle.training.exceptions.AccessDeniedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import static com.oracle.oracle.training.constants.Constants.API_SECRET_KEY;

@Slf4j
public class AuthFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse) response;
        String authHeader = req.getHeader("Authorization");
        if(authHeader!=null){
            String[] authHeaderArr = authHeader.split("Bearer ");
            if(authHeaderArr.length > 1 && authHeaderArr[1]!=null){
                String token = authHeaderArr[1];
                try{
                    Claims claims = Jwts.parser().setSigningKey(API_SECRET_KEY).parseClaimsJws(token).getBody();
                    req.setAttribute("userId",Integer.parseInt(claims.get("userId").toString()));
                    req.setAttribute("email",(String)claims.get("email"));
                }catch (Exception e){
                    log.error("Invalid/Expired Token");
                    res.sendError(res.SC_FORBIDDEN,"Unauthorized");
                    return;
                }
            }else {
                log.error("Authorization token must be Bearer [token]");
                res.sendError(res.SC_FORBIDDEN,"Unauthorized");
                return;
            }
        }else {
            log.error("Authorization Header is missing!");
            res.sendError(res.SC_FORBIDDEN,"Unauthorized");
            return;
        }
        chain.doFilter(request,response);
    }
}
