package ar.com.miura.usersapi.service;

import ar.com.miura.usersapi.misc.EnvReader;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtService {

    public static final String JWT_KEY = "jwt.key";
    public static final String JWT_TOKEN_VALIDITY = "jwt.token.validity";
    public static final String CLAIM_AUTHORITIES = "authorities";
    public static final String DEFAULT_ROLE_VALUES = "role_1,role_2,role_3";
    private EnvReader envReader;

    @Autowired
    public JwtService(EnvReader envReader) {
        this.envReader = envReader;
    }

    /**
     *
     * @param subject
     * @return
     */
    public String getToken(String subject) {
        String stringKey = envReader.getProperty(JWT_KEY);
        Integer seconds = Integer.valueOf(envReader.getProperty(JWT_TOKEN_VALIDITY));

        Long nowTime = System.currentTimeMillis();

        SecretKey key = Keys.hmacShaKeyFor(stringKey.getBytes(StandardCharsets.UTF_8));

        Claims claims = new DefaultClaims();
        claims.put(CLAIM_AUTHORITIES, DEFAULT_ROLE_VALUES);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(nowTime))
                .setExpiration(new Date(System.currentTimeMillis() + seconds * 1000))
                .signWith(key, SignatureAlgorithm.HS256)
                .signWith(key)
                .compact();

    }

    /**
     *
     * @param jwt
     * @return
     */
    public Claims readJwtToken(String jwt) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(envReader.getProperty(JWT_KEY).getBytes(StandardCharsets.UTF_8));
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
        }catch(Exception e) {
            throw new BadCredentialsException("Invalid Token received!");
        }

    }

}
