package com.soshiant.springbootexample.controller;

import static com.soshiant.springbootexample.util.AppConstants.INTERNAL_SERVER_ERROR_MESSAGE;
import static com.soshiant.springbootexample.util.AppConstants.LOGIN_INVALID_USER_ERROR_MESSAGE;
import static com.soshiant.springbootexample.util.AppConstants.LOGIN_USER_DISABLED_ERROR_MESSAGE;
import static com.soshiant.springbootexample.util.AppConstants.LOGIN_USER_LOCKED_ERROR_MESSAGE;
import static com.soshiant.springbootexample.util.AppConstants.LOGIN_USER_NOT_FOUND_ERROR_MESSAGE;

import com.soshiant.springbootexample.dto.LoginRequestDto;
import com.soshiant.springbootexample.dto.LoginResponseDto;
import com.soshiant.springbootexample.service.AuthenticationService;
import com.soshiant.springbootexample.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Hamid.Ghorbani
 */

@Slf4j
@Controller
@Validated
@RequestMapping("/authenticate")
public class AuthenticationController {

  @Autowired
  private AuthenticationService authenticationService;



  /**
   * processes a login request
   *
   * @param loginRequestDto LoginDto
   * @return user Info
   */
  @ApiOperation(value = "Processes incoming login")
  @PostMapping( value = "/login",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseBody
  public ResponseEntity<Object> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
    log.info("Received a login request for user : [{}]", loginRequestDto.getUsername());

    String responseBody = "";
    try {
      LoginResponseDto loginResponseDto = authenticationService.authenticate(loginRequestDto.getUsername(),
          loginRequestDto.getPassword());

      if (loginResponseDto == null) {
        log.error(" authenticationService.authenticate result is null for user [{}]",
                  loginRequestDto.getUsername());
        throw new UsernameNotFoundException(LOGIN_USER_NOT_FOUND_ERROR_MESSAGE);
      }
      responseBody = ResponseUtil.createLoginSuccessResponse(loginResponseDto);
      return new ResponseEntity<>(responseBody, HttpStatus.OK);

    } catch (UsernameNotFoundException e) {
      log.error("UsernameNotFoundException for user {}, exception message:{}",
          loginRequestDto.getUsername(), e);
      responseBody = ResponseUtil.createErrorResponse(LOGIN_USER_NOT_FOUND_ERROR_MESSAGE,"");
      return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);

    } catch (BadCredentialsException e) { // invalid login, update user attempts
      log.error("BadCredentialsException, login request for user {}, exception message:{}",
          loginRequestDto.getUsername(), e);
      responseBody = ResponseUtil.createErrorResponse(LOGIN_INVALID_USER_ERROR_MESSAGE,"");
      return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);

    } catch (LockedException e) { // this user is locked
      log.error("LockedException, login request for user {}, exception message:{}",
          loginRequestDto.getUsername(), e);
      responseBody = ResponseUtil.createErrorResponse(LOGIN_USER_LOCKED_ERROR_MESSAGE,"");
      return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);

    } catch (DisabledException e) {
      log.error("DisabledException, login request for user {}, exception message:{}",
          loginRequestDto.getUsername(), e);
      responseBody = ResponseUtil.createErrorResponse(LOGIN_USER_DISABLED_ERROR_MESSAGE,"");
      return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);

    } catch (Exception e) {
      log.error("Error processing incoming login request for user {}, exception message:{}",
          loginRequestDto.getUsername(), e);
      return new ResponseEntity<>(INTERNAL_SERVER_ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * processes a logout request
   *
   * @param username String
   * @return Auth Info
   */
  @ApiOperation(value = "Processes incoming logout")
  @GetMapping(value = "/logout",
      params ={"username"},
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<Object> logout(@Valid @RequestParam(value = "username") String username) {
    log.info("Received a logout [username{}]...", username);

    try {
      boolean result = authenticationService.logout(username);

      if (!result) {
        log.error("couldn't logout for user {}", username);
        return new ResponseEntity<>(
            ResponseUtil.createErrorResponse("couldn't logout!",username),
            HttpStatus.EXPECTATION_FAILED);
      }
      return new ResponseEntity<>(
          ResponseUtil.createSuccessResponse("logout successfully.",username),
          HttpStatus.OK);

    } catch (Exception e) {
      log.error("Error processing incoming logout request for user {}, exception message:{}",username, e);
      return new ResponseEntity<>(
          ResponseUtil.createErrorResponse("couldn't logout!",null),
          HttpStatus.EXPECTATION_FAILED);
    }
  }


}
