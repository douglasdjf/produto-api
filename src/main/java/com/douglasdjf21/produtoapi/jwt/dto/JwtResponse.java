package com.douglasdjf21.produtoapi.jwt.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private Integer id;
    private String name;
    private String email;

    public static JwtResponse getUser(Claims jwtClaims) {
        try {
            log.info("getUser");
            JwtResponse authClaims = new ObjectMapper().convertValue(jwtClaims.get("authUser"), JwtResponse.class);
            log.info("authClaims:" +authClaims);
            return authClaims ;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
