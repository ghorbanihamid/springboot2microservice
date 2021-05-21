package com.soshiant.springbootexample.filter;

import com.soshiant.springbootexample.security.JwtTokenProvider;
import com.soshiant.springbootexample.service.AuthenticationService;
import com.soshiant.springbootexample.service.UserService;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * A filter which logs all requests.
 *
 */

@Slf4j
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private AuthenticationService authenticationService;

  @Value("${jwt.token.security.header.key}")
  private String authenticationHeaderKey;

  @Value("${jwt.token.security.header.prefix}")
  private String authenticationHeaderPrefix;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      SecurityContextHolder.clearContext();
      UsernamePasswordAuthenticationToken authenticationToken =
          authenticationService.getAuthenticationFromJWTToken(extractJWTToken(request));
      if(authenticationToken != null) {
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      }

    } catch (Exception e){
      log.error("AuthenticationFilter, exception:{}",e.getMessage());
    }
    filterChain.doFilter(request, response);
  }

  public String extractJWTToken(HttpServletRequest request) {

    String jwtHeader = request.getHeader(authenticationHeaderKey);
    if (!StringUtils.isBlank(jwtHeader) && jwtHeader.startsWith(authenticationHeaderPrefix)) {
      return jwtHeader.substring(7).trim();
    }
    return null;
  }
}
