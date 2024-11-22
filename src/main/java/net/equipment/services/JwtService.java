package net.equipment.services;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Service class responsible for generating and validating JSON Web Tokens (JWT) for user authentication.
 * It handles token creation, extraction of claims, and validation of tokens.
 */
@RequiredArgsConstructor
@Service
public class JwtService {

    @Value("${token.secret.key}")
    private String jwtSecretKey;  // Secret key used for signing the JWT

    @Value("${token.expirationms}")
    private Long jwtExpirationMs;  // Expiration time for the JWT in milliseconds

    /**
     * Extracts the username (subject) from the given JWT token.
     *
     * @param token the JWT token
     * @return the username (subject) extracted from the token
     */
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Generates a JWT token for a given user details.
     *
     * @param userDetails the user details for which the token will be generated
     * @return the generated JWT token
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Validates the given JWT token by comparing the username from the token with the user details
     * and checking whether the token has expired.
     *
     * @param token the JWT token to validate
     * @param userDetails the user details for comparison
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Extracts a specific claim from the JWT token.
     *
     * @param token the JWT token
     * @param claimsResolvers a function to resolve the claim (e.g., subject, expiration)
     * @param <T> the type of the claim
     * @return the claim extracted from the token
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * Generates a JWT token with additional custom claims.
     *
     * @param extraClaims additional claims to include in the JWT token
     * @param userDetails the user details for the token subject
     * @return the generated JWT token
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Token issue time
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))  // Token expiration time
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)  // Signing the token with the secret key
                .compact();
    }

    /**
     * Checks if the given JWT token is expired.
     *
     * @param token the JWT token
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date of the JWT token.
     *
     * @param token the JWT token
     * @return the expiration date of the token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token the JWT token
     * @return the claims contained in the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())  // Set the signing key to verify the token
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Retrieves the signing key for signing and validating the JWT.
     *
     * @return the signing key used for JWT creation and validation
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);  // Decode the secret key from BASE64
        return Keys.hmacShaKeyFor(keyBytes);  // Return the HMAC SHA key for signing
    }
}

