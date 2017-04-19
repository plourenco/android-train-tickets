package com.example.util;

import com.example.dao.UserManager;
import com.example.exceptions.InvalidUserDataException;
import com.example.models.TokenModel;
import com.example.models.UserModel;
import com.example.models.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.ws.rs.core.Response;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

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

    public static boolean isTrustworthy(String issuer, String token) {
        if (TokenHelper.isValid(token)) { // check if token is valid
            if(Objects.equals(TokenHelper.getIssuer(token), issuer)) {
                String email = TokenHelper.getEmail(token);
                UserRole role = UserRole.idToRole(TokenHelper.getRole(token));
                UserManager manager = new UserManager();

                if (email != null && role != null) {
                    // check if token identifies a user
                    UserModel model = manager.getUserByEmail(email);
                    // check if user exists
                    if (model != null) {
                        // check if token role corresponds to user role
                        if (role.id() == model.getRoleUser()) {
                            // if role changes, generate a new token
                            return true;
                        }
                    }
                }
            }
        }
        return false;
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
