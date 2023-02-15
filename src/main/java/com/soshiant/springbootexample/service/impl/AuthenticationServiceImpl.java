package com.soshiant.springbootexample.service.impl;


import com.soshiant.springbootexample.dto.LoginResponseDto;
import com.soshiant.springbootexample.service.AuthenticationService;
import com.soshiant.springbootexample.service.UserService;
import com.soshiant.springbootexample.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

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
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            UserDetails userDetails = userService.loadUserByUsername(username);
            if (userDetails == null) {
                throw new UsernameNotFoundException("Username Not Found");
            }
            LoginResponseDto loginResponseDto = new LoginResponseDto();
            loginResponseDto.setUsername(userDetails.getUsername());
            String jwtToken = jwtTokenUtil.generateToken(userDetails);
            loginResponseDto.setJwtToken(jwtToken);
            return loginResponseDto;

        } catch (Exception e) {
            log.error("Exception in login, username: {}, password: {}, message: {}. ",
                    username, password, e.getMessage());
            throw e;
        }

    }

    /**
     * @param username String
     * @return Auth Info
     */
    public boolean logout(String username) throws Exception {
        return true;
    }

    public UsernamePasswordAuthenticationToken authenticateJWTToken(String jwtToken) {
        try {
            if (StringUtils.isBlank(jwtToken)) {
                return null;
            }

            if (jwtTokenUtil.isTokenExpired(jwtToken)) {
                return null;
            }

            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            if (!StringUtils.isBlank(username)) {
                UserDetails userDetails = userService.loadUserByUsername(username);
                if (userDetails != null) {
                    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                }
            }
            return null;
        } catch (Exception e) {
            log.error("AuthenticationService.getAuthenticationFromJWTToken,token{}, exception Message:{}", jwtToken, e.getMessage());
            throw e;
        }
    }
}
