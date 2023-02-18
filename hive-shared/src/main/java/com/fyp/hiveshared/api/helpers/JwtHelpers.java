package com.fyp.hiveshared.api.helpers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.security.SignatureException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class JwtHelpers {

    public static boolean isAccessTokenExpired(String jwtToken) {
        try {
            return JWT.decode(jwtToken).getExpiresAt().before(new Date());
        } catch (JWTDecodeException ex) {
            return true;
        }
    }

    public static boolean isValidSignature(String jwtToken, String jwtSecret) {
        try {
            JWT.require(Algorithm.HMAC256(jwtSecret)).build().verify(jwtToken);
            return true;
        } catch (JWTVerificationException ex){
            return false;
        }
    }

    public static Optional<String> getPayload(String jwtToken, String jwtSecret, String claim) {
        if (!isAccessTokenExpired(jwtToken) && isValidSignature(jwtToken, jwtSecret)) {
            return Optional.of(JWT.decode(jwtToken).getClaim(claim).asString());
        }
        return null;
    }

//    public static String getPayloads(String jwtToken, List<String> claims) {
//
//    }
}
