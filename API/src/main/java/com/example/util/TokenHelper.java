package com.example.util;

import com.example.exceptions.InvalidUserDataException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class TokenHelper {

    private static final String secret = "itshardtoguessthis";

    /**
     * Generate a new token
     * @param email String
     * @param role int
     * @param expires Date
     */
    public static String getJWTString(String email, int role, Date expires) {
        if (email == null || role <= 0 || expires == null) {
            throw new InvalidUserDataException("Illegal token");
        }
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        return Jwts.builder()
                .setIssuer("TrainTickets-Security")
                .setSubject(email)
                .setAudience(String.valueOf(role))
                .setExpiration(expires)
                .setIssuedAt(new Date())
                .signWith(signatureAlgorithm, secret)
                .compact();
    }

    /**
     * Generate a new refresh token
     * @param email String
     * @param role int
     */
    public static String getJWTRefresh(String email, int role) {
        if (email == null || role <= 0) {
            throw new InvalidUserDataException("Illegal token");
        }
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        return Jwts.builder()
                .setIssuer("TrainTickets-Refresh")
                .setSubject(email)
                .setAudience(String.valueOf(role))
                .setIssuedAt(new Date())
                .signWith(signatureAlgorithm, secret)
                .compact();
    }

    /**
     * Check if the token is valid
     * @param token String
     */
    public static boolean isValid(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token.trim());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get the username from the token
     * @param jwsToken String
     */
    public static String getEmail(String jwsToken) {
        if (isValid(jwsToken)) {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwsToken);
            return claimsJws.getBody().getSubject();
        }
        return null;
    }

    /**
     * Get the role from the token
     * @param jwsToken String
     */
    public static int getRole(String jwsToken) {
        if (isValid(jwsToken)) {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwsToken);
            String roleStr = claimsJws.getBody().getAudience();
            try {
                return Integer.parseInt(roleStr);
            }
            catch(NumberFormatException ignored) { }
        }
        return -1;
    }

    public static String getIssuer(String jwsToken) {
        if (isValid(jwsToken)) {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwsToken);
            return claimsJws.getBody().getIssuer();
        }
        return null;
    }
}
