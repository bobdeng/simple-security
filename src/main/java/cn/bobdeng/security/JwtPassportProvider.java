package cn.bobdeng.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;

import java.util.Date;

public class JwtPassportProvider implements PassportProvider {
    private String prefix;
    @Getter
    private long expireAfter;
    private String secretKey;

    public JwtPassportProvider(String prefix, long expireAfter, String secretKey) {
        this.prefix = prefix;
        this.expireAfter = expireAfter;
        this.secretKey = secretKey;
    }

    @Override
    public String from(String value) {
        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(value.substring(prefix.length()));
        return jws.getBody().getSubject();

    }

    @Override
    public String to(String subject) {
        return prefix + Jwts.builder()
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + expireAfter))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
