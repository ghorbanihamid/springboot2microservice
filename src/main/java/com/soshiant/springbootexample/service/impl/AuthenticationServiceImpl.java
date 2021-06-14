package com.soshiant.springbootexample.service.impl;


import com.soshiant.springbootexample.dto.LoginResponseDto;
import com.soshiant.springbootexample.entity.Customer;
import com.soshiant.springbootexample.mapper.CustomerMapper;
import com.soshiant.springbootexample.security.JwtTokenProvider;
import com.soshiant.springbootexample.service.AuthenticationService;
import com.soshiant.springbootexample.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  private final CustomerMapper customerMapper = Mappers.getMapper(CustomerMapper.class);
//  private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

  @Autowired
  private CustomerService customerService;

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private AuthenticationManager authenticationManager;

  /**
   * @param username String
   * @param password String, BCrypt algorithm generates a String of length 60
   * @return Auth Info
   */
  public LoginResponseDto authenticate(String username, String password) throws Exception {

    try {
       authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username,password)
        );
      Customer customerInfo = customerService.getCustomerByUsername(username);
        UserDetails userDetails =
            new User( username,
                      customerInfo.getUserInfo().getPassword(),
                      customerInfo.getUserInfo().isEnabled(),
                      true,
                      true,
                      !customerInfo.getUserInfo().isLocked(),
                      AuthorityUtils.createAuthorityList("ROLE_USER")
            );
        LoginResponseDto loginResponseDto = customerMapper.toLoginResponse(customerInfo);
        String jwtToken  = jwtTokenProvider.generateToken(userDetails);
        loginResponseDto.setJwtToken(jwtToken);
        return loginResponseDto;

    } catch (Exception e){
      log.error("Exception in login, username: {}, password: {}, message: {}. ",
          username, password, e.getMessage());
      throw e;
    }

  }

  /**
   * @param username String
   * @return Auth Info
   */
  public boolean logout(String username) throws Exception{
    return true;
  }

  public UsernamePasswordAuthenticationToken getAuthenticationFromJWTToken(String jwtToken) {

    if(StringUtils.isBlank(jwtToken)){
      return null;
    }
    String username = jwtTokenProvider.extractUsername(jwtToken);
    if (!StringUtils.isBlank(username)) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    return null;
  }
}
