package com.huke.util;

import com.huke.config.AppProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author huke
 * @date 2022/08/29/下午2:03
 */
@AllArgsConstructor
@Component
public class JwtUtil {

    //用于签名访问令牌的密钥
    public static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    //用于签名刷新令牌的密钥
    static final Key refreshKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private final AppProperties appProperties;

    public String createAccessToken(UserDetails userDetails) {
        return creatJwtToken(userDetails, appProperties.getJwt().getAccessTokenExpireTime(), key);
    }

    public String createRefreshToken(UserDetails userDetails) {
        return creatJwtToken(userDetails, appProperties.getJwt().getRefreshTokenExpireTime(), refreshKey);
    }


    public String createAccessTokenRefreshToken(String token) {
        return parseClaims(token, refreshKey)
                .map(claims -> Jwts.builder()
                        .setClaims((Claims) claims)
                        .setExpiration(new Date(System.currentTimeMillis() + appProperties.getJwt().getAccessTokenExpireTime()))
                        .setIssuedAt(new Date())
                        .signWith(key, SignatureAlgorithm.HS512)
                        .compact())
                .orElseThrow(() -> new AccountExpiredException("访问被决绝"));
    }

    public Optional<Jws<Claims>> parseClaims(String token, Key key) {
        try {
            val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return Optional.of(claims);

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, key, true);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshKey, true);
    }

    public boolean validateAccessTokenWithOutExpireTime(String token) {
        return validateToken(token, key, true);
    }

    private boolean validateToken(String token, Key key, boolean isExpiredInvalid) {

        try {
            Jwt parse = Jwts.parserBuilder().setSigningKey(key).build().parse(token);
            return Objects.nonNull(parse) ? true : false;
        } catch (Exception e) {

            return isExpiredInvalid;
        }
    }


    public String creatJwtToken(UserDetails userDetails, long expireTime, Key key) {
        val now = System.currentTimeMillis();
        return Jwts.builder()
                .setId("")
                .claim("authorities", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 60_000))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}
