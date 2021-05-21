package com.soshiant.springbootexample.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Component
public class JwtTokenProvider {

  /**
   * For simplicity, we are storing a static key here. Ideally, in a
   * microservices environment, this key would be kept on a config-server.
   */
  @Value("${jwt.token.secret-key:secret-key}")
  private String secretKey;

  @Value("${jwt.token.expire-time:3600000}")
  private long validityInMilliseconds; // 1h


  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }


  public String generateToken(UserDetails userDetails) {

    Claims claims = Jwts.claims().setSubject(userDetails.getUsername());

    claims.put("auth", userDetails.getAuthorities()
                                  .stream()
                                  .map(s -> new SimpleGrantedAuthority(s.getAuthority()))
                                  .filter(Objects::nonNull)
                                  .collect(Collectors.toList()));
    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();
  }

  public Claims extractClaims(String token) {
    try {
      return !StringUtils.isBlank(token) ?
          Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody() : null;

    } catch (JwtException | IllegalArgumentException e) {
      return null;
    }
  }

  /*
   * { "subject": "username"}
   */
  public String extractUsername(String token) {
    Claims claims = extractClaims(token);
    return claims != null ? claims.getSubject() : null;
  }

  public Date extractExpiration(String token) {
    Claims claims = extractClaims(token);
    return claims != null ? claims.getExpiration() : null;
  }

}
