package com.soshiant.springbootexample.handler;

import com.soshiant.springbootexample.entity.UserInfo;
import com.soshiant.springbootexample.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  @Autowired
  private UserService userService;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication)  {

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    UserInfo userInfo = userService.findUserByUsername(userDetails.getUsername());

    if (userInfo.getFailedAttempt() > 0) {
      userService.resetPasswordRetryAttempts(userDetails.getUsername());
    }

  }

}
