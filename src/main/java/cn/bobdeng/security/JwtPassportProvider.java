package cn.bobdeng.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtPassportProvider implements PassportProvider {
    private String prefix = "Bearer ";
    private long expireAfter = 86400 * 1000;
    private String secretKey = "this is a secret";

    public JwtPassportProvider(String prefix, long expireAfter, String secretKey) {
        this.prefix = prefix;
        this.expireAfter = expireAfter;
        this.secretKey = secretKey;
    }

    public JwtPassportProvider() {
    }

    @Override
    public String fromCookie(String value) {
        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(value.substring(prefix.length()));
        return jws.getBody().getSubject();

    }

    @Override
    public String toCookieValue(Passport passport) {
        return prefix + Jwts.builder()
                .setSubject(passport.toStringValue())
                .setExpiration(new Date(System.currentTimeMillis() + expireAfter))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
