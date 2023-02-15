package com.soshiant.springbootexample.controller;

import com.soshiant.springbootexample.dto.LoginRequestDto;
import com.soshiant.springbootexample.dto.LoginResponseDto;
import com.soshiant.springbootexample.dto.ResponseDto;
import com.soshiant.springbootexample.service.AuthenticationService;
import com.soshiant.springbootexample.util.JacksonUtils;
import com.soshiant.springbootexample.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

import static com.soshiant.springbootexample.util.AppConstants.*;

/**
 *
 * @author Hamid.Ghorbani
 */

@Slf4j
@Controller
@Validated
public class AuthenticationController {

  @Autowired
  private AuthenticationService authenticationService;

  /**
   * show login form
   */
  @GetMapping("/login")
  public String showLoginPage(Model model) {
    LoginRequestDto loginRequestDto = new LoginRequestDto();
    model.addAttribute("loginRequestDto", loginRequestDto);
    return "login";
  }

  /**
   * processes a login request
   *
   * @param loginRequestDto LoginDto
   * @return user Info
   */
  @Operation(summary = "Processes incoming login")
  @PostMapping( value = "/authenticate",
                consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE
  )
  public String loginFormData(@Valid @ModelAttribute("loginRequestDto") LoginRequestDto loginRequestDto, Model model) {

    try {
      ResponseEntity<Object> response = login(loginRequestDto);
      if (response.getStatusCode() == HttpStatus.OK) {
        LoginResponseDto loginResponseDto = (LoginResponseDto) JacksonUtils.toObject(String.valueOf(response.getBody()), ResponseDto.class);
        String token = "";
        if(loginResponseDto != null) {
          token = StringUtils.defaultString(String.valueOf(loginResponseDto.getJwtToken()), "");
        }
        HashMap<String, String> userMenuMap = new HashMap<>();
        userMenuMap.put("Add New Customer", "customer/register-page");
        userMenuMap.put("Customers List", "customer/customer-info");

        HashMap<String, String> adminMenuMap = new HashMap<>();
        adminMenuMap.put("Add New Employee", "employee/register-page");
        adminMenuMap.put("Employee List", "employee/customer-info");

        model.addAttribute("userMenuMap", userMenuMap);
        model.addAttribute("adminMenuMap", adminMenuMap);
        model.addAttribute("token", token);
        return DASHBOARD_HTML;
      } else {
        ResponseDto responseDto = (ResponseDto) JacksonUtils.toObject(String.valueOf(response.getBody()), ResponseDto.class);
        String errorMessage = "";
        errorMessage = StringUtils.defaultString(String.valueOf(responseDto.getError()), "");
        model.addAttribute(MESSAGE, errorMessage);
        return LOGIN_HTML;
      }
    } catch (Exception e) {
      log.error("Exception, login request for user {}, exception message:{}",
              loginRequestDto.getUsername(), e);
      model.addAttribute(MESSAGE, e.getMessage());
      return LOGIN_HTML;
    }
  }

  /**
   * processes a login request
   *
   * @param loginRequestDto LoginDto
   * @return user Info
   */
  @Operation(summary = "Processes incoming login")
  @PostMapping( value = "/authenticate",
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
      return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);

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
  @Operation(summary = "Processes incoming logout")
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
          ResponseUtil.createSuccessResponse(username),
          HttpStatus.OK);

    } catch (Exception e) {
      log.error("Error processing incoming logout request for user {}, exception message:{}",username, e);
      return new ResponseEntity<>(
          ResponseUtil.createErrorResponse("couldn't logout!",null),
          HttpStatus.EXPECTATION_FAILED);
    }
  }
}
