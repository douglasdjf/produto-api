package com.douglasdjf21.produtoapi.jwt.service;

import com.douglasdjf21.produtoapi.exception.AuthenticationException;
import com.douglasdjf21.produtoapi.jwt.dto.JwtResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.lang.Strings;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
@Service
public class JwtService {

    private static final String BEARER= "bearer ";

    private static final String EMPTY_SPACE = " ";
    private static final Integer TOKEN_INDEX = 1;

    @Value("${app-config.secrets.api-secret}")
    private String apiSecret;

    public void validateAuthorization(String token) {
        var accessToken = extractToken(token);
        try {
            var claims = Jwts
                    .parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(apiSecret.getBytes()))
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
            var user = JwtResponse.getUser(claims);
            if (ObjectUtils.isEmpty(user) || ObjectUtils.isEmpty(user.getId())) {
                throw new AuthenticationException("Usuário inválido");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new AuthenticationException("Erro ao processar Access Token");
        }
    }

    private String extractToken(String token) {
        if (!Strings.hasText(token)) {
            throw new AuthenticationException(" Access Token não informado");
        }
        if (token.contains(EMPTY_SPACE)) {
            return token.split(EMPTY_SPACE)[TOKEN_INDEX];
        }
        return token;
    }
}
