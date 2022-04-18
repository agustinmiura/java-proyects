package ar.com.miura.usersapi.filter;

import ar.com.miura.usersapi.constants.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

public class JWTTokenValidatorFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, javax.servlet.FilterChain filterChain) throws java.io.IOException, javax.servlet.ServletException {
        String jwt = request.getHeader(SecurityConstants.JWT_HEADER);
        if (null != jwt) {
            try {
                SecretKey key = Keys.hmacShaKeyFor(
                        SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();
                String username = String.valueOf(claims.get(Claims.SUBJECT));
                String authorities = (String) claims.get("authorities");
                Authentication auth = new UsernamePasswordAuthenticationToken(username,null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }catch (Exception e) {
                throw new BadCredentialsException("Invalid Token received!");
            }

        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/health");
    }
}
