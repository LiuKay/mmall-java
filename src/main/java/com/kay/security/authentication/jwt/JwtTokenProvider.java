package com.kay.security.authentication.jwt;

import com.google.common.base.Preconditions;
import com.kay.domain.Role;
import com.kay.security.JwtAuthenticationException;
import com.kay.vo.UserIdentityDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProvider {

    private static final String AUTH_NAME = "auth";
    private static final String USER_ID_NAME = "userId";

    /**
     * @apiNote :
     * THIS IS NOT A SECURE PRACTICE! For simplicity, we are storing a static key here. Ideally, in a
     * microservices environment, this key would be kept on a config-server.
     */
    @Value("${application.security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000; // 1h

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String username, Integer userId, Role role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(AUTH_NAME, role);
        claims.put(USER_ID_NAME, userId);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, secretKey)//
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
            Claims body = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            Integer userId = body.get(USER_ID_NAME, Integer.class);
            String roleName = body.get(AUTH_NAME, String.class);
            return new UserIdentityDTO(body.getSubject(), userId, Role.valueOf(roleName));
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException |
                SignatureException | IllegalArgumentException exception) {
            throw new JwtAuthenticationException("Token is invalid", exception);
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
