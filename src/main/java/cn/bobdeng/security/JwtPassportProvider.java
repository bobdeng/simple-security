package cn.bobdeng.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtPassportProvider implements PassportProvider {
    private static final String PREFIX = "Bearer ";
    private static final long EXPIRE = 86400 * 1000;
    public static final String BASE_64_ENCODED_SECRET_KEY = "P%LtIprLOSXT$ZZuMwzjTvBgDuBR3@K9";

    @Override
    public String fromCookie(String value) {
        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(BASE_64_ENCODED_SECRET_KEY)
                .parseClaimsJws(value.substring(PREFIX.length()));
        return jws.getBody().getSubject();

    }

    @Override
    public String toCookieValue(Passport passport) {
        System.out.println(passport.toStringValue());
        return PREFIX + Jwts.builder()
                .setSubject(passport.toStringValue())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .signWith(SignatureAlgorithm.HS256, BASE_64_ENCODED_SECRET_KEY)
                .compact();
    }
}
