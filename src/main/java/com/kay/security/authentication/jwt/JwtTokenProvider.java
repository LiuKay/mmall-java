package com.kay.security.authentication.jwt;

import com.google.common.base.Preconditions;
import com.kay.config.AppConfigProperties;
import com.kay.domain.Role;
import com.kay.security.JwtAuthenticationException;
import com.kay.vo.UserIdentityDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProvider {

    private static final String AUTH_NAME = "auth";
    private static final String USER_ID_NAME = "userId";

    @Autowired
    private AppConfigProperties appConfigProperties;

    public String createToken(String username, Integer userId, Role role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(AUTH_NAME, role);
        claims.put(USER_ID_NAME, userId);

        Date now = new Date();
        Date validity = new Date(now.getTime() + appConfigProperties.getJwt().getValidityInMilliseconds());

        SecretKey secretKey = Keys
                .hmacShaKeyFor(appConfigProperties.getJwt().getSecretKey().getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                   .setClaims(claims)
                   .setIssuedAt(now)
                   .setExpiration(validity)
                   .signWith(secretKey, SignatureAlgorithm.HS256)
                   .compact();
    }

    public UserIdentityDTO getUserIdentity(HttpServletRequest req) {
        String token = resolveToken(req);
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        return validateAndDecode(token);
    }

    public UserIdentityDTO validateAndDecode(String token) {
        Preconditions.checkNotNull(token, "token can not be null.");
        try {
            byte[] keyBytes = appConfigProperties.getJwt().getSecretKey().getBytes(StandardCharsets.UTF_8);
            Claims body = Jwts.parserBuilder()
                              .setSigningKey(keyBytes)
                              .build()
                              .parseClaimsJws(token)
                              .getBody();
            Integer userId = body.get(USER_ID_NAME, Integer.class);
            String roleName = body.get(AUTH_NAME, String.class);
            return new UserIdentityDTO(body.getSubject(), userId, Role.valueOf(roleName));
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException |
                SecurityException | IllegalArgumentException exception) {
            throw new JwtAuthenticationException(String.format("Token is invalid:%s", exception.getMessage()),
                                                 exception);
        }
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
