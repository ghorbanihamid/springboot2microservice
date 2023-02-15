package com.soshiant.springbootexample.filter;

import com.soshiant.springbootexample.service.AuthenticationService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import static com.soshiant.springbootexample.util.AppConstants.AUTHORIZATION;
import static com.soshiant.springbootexample.util.AppConstants.BEARER;

/**
 * A filter which controls permission on requests.
 *
 */

@Slf4j
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private AuthenticationService authenticationService;


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                                                                  throws ServletException, IOException {

    String jwtToken = null;
    try {
      SecurityContextHolder.clearContext();

      jwtToken = getJWTTokenFromRequest(request);
      if(!StringUtils.isBlank(jwtToken)) {
        // Once we get the token validate it.
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                                  authenticationService.authenticateJWTToken(jwtToken);

        if (usernamePasswordAuthenticationToken != null) {
          usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          // After setting the Authentication in the context, we specify
          // that the current user is authenticated. So it passes the
          // Spring Security Configurations successfully.
          SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
      } else {
        logger.warn("Request doesn't have JWT Token!");
      }

    } catch (Exception e){
      log.error("AuthenticationFilter, exception:{}",e.getMessage());
    }
    filterChain.doFilter(request, response);
  }

  public String getJWTTokenFromRequest(HttpServletRequest request) {

    String jwtHeader = request.getHeader(AUTHORIZATION);
    if (!StringUtils.isBlank(jwtHeader) && jwtHeader.startsWith(BEARER)) {
      return jwtHeader.substring(7).trim();
    }
    return null;
  }
}
