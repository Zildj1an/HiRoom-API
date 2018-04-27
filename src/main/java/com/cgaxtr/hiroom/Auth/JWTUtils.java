package com.cgaxtr.hiroom.Auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cgaxtr.hiroom.Utils.Config;

import java.io.UnsupportedEncodingException;

public class JWTUtils {

    public static String issueToken(String email){

        String token;

        try {

            Algorithm algorithm = Algorithm.HMAC256(Config.KEYJWT);
            token = JWT.create()
                    .withIssuer(Config.ISSUER)
                    .withSubject(email)
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("You need to enable Algorithm.HMAC256");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }

        return token;
    }

    public static void validateToken(String token){

        try {

            Algorithm algorithm = Algorithm.HMAC256(Config.KEYJWT);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(Config.ISSUER)
                    .build();
            DecodedJWT jwt = verifier.verify(token);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}