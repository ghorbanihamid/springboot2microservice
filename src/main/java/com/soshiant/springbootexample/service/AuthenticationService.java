package com.soshiant.springbootexample.service;


import com.soshiant.springbootexample.dto.LoginResponseDto;
import com.soshiant.springbootexample.entity.UserInfo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 *
 * @author Hamid.Ghorbani
 *
 */
public interface AuthenticationService {

  /**
   * @param username String
   * @param password String
   * @return Auth Info
   */
  LoginResponseDto authenticate(String username, String password) throws Exception;

  /**
   * @param username String
   * @return Auth Info
   */
  boolean logout(String username) throws Exception;

  public UsernamePasswordAuthenticationToken authenticateJWTToken(String jwtToken);
}
