package com.fyp.hiveshared.api.helpers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fyp.hiveshared.api.config.JwtSecret;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

public class JwtHelpers {

    private JwtSecret jwtSecret = new JwtSecret();

    public JwtHelpers() {

    }

    public String generateJwtTokenWithPayload(Map<String, String> payload) {
        Date now = new Date(System.currentTimeMillis());
        return JWT.create()
                .withIssuedAt(now)
                .withExpiresAt(getTokenExpireTime(now))
                .withPayload(payload)
                .sign(Algorithm.HMAC256(jwtSecret.getJwtSecret()));
    }

    public boolean isValidJwtToken(String jwtToken) {
        return isAccessTokenExpired(jwtToken) || isValidSignature(jwtToken);
    }

    public boolean isAccessTokenExpired(String jwtToken) {
        try {
            return !JWT.decode(jwtToken).getExpiresAt().before(new Date());
        } catch (JWTDecodeException ex) {
            return false;
        }
    }

    public boolean isValidSignature(String jwtToken) {
        try {
            JWT.require(Algorithm.HMAC256(jwtSecret.getJwtSecret())).build().verify(jwtToken);
            return true;
        } catch (JWTVerificationException ex){
            return false;
        }
    }

    public Optional<String> getPayload(String jwtToken, String claim) {
        if (isAccessTokenExpired(jwtToken) && isValidSignature(jwtToken)) {
            return Optional.of(JWT.decode(jwtToken).getClaim(claim).asString());
        }
        return Optional.empty();
    }

    private Date getTokenExpireTime(Date now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.HOUR_OF_DAY, 24);
        return calendar.getTime();
    }
}
