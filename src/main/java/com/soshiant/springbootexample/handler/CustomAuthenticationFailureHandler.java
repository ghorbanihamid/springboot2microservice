package com.soshiant.springbootexample.handler;

import com.soshiant.springbootexample.entity.UserInfo;
import com.soshiant.springbootexample.service.UserService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

  @Value("${security.max.login.failed.attempts}")
  private String loginFailedAttempts;

  @Autowired
  private UserService userService;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                AuthenticationException exception) throws IOException, ServletException {

    String username = request.getParameter("username");
    UserInfo user = userService.findUserByUsername(username);

    if (user != null) {
      if (user.isEnabled() && !user.isLocked()) {
        if (user.getFailedAttempt() < Integer.parseInt(loginFailedAttempts) - 1) {
          userService.increasePasswordRetryAttempts(user.getUsername());
        } else {
          userService.lockUser(user.getUsername());
          exception = new LockedException("Your account has been locked due to 3 failed attempts."
              + " It will be unlocked after 24 hours.");
        }
      } else if (user.isLocked()) {
//        if (userService.unlockWhenTimeExpired(user)) {
//          exception = new LockedException("Your account has been unlocked. Please try to login again.");
//        }
      }
    }
  }
}
