package com.soshiant.springbootexample.controller;

import com.soshiant.springbootexample.dto.SignupDto;
import com.soshiant.springbootexample.service.UserService;
import com.soshiant.springbootexample.util.DataUtils;
import com.soshiant.springbootexample.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping(value = {"/dashboard"})
  public String dashboard(Model model){
    HashMap<String, String> menuMap = new HashMap<>();
    menuMap.put("Add New Customer", "customer/register-page");
    menuMap.put("Customers List", "customer/customer-info");
    menuMap.put("Logout", "logout");
    model.addAttribute("menuMap", menuMap);
    return DASHBOARD_HTML;
  }

  @RequestMapping(value = {"/signup-page"}, method = RequestMethod.GET)
  public String showSignupPage(Model model){
    model.addAttribute("signupDto", new SignupDto());
    return "signup";
  }

  /**
   * processes a login request
   *
   * @param signupDto SignupDto
   * @return success
   */
  @Operation(summary = "Processes incoming signup request")
  @PostMapping( value = "/signup",
          consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE
  )
  public String signup(@Valid @ModelAttribute("signupDto")  SignupDto signupDto, Model model) {
    log.info("Received a signup request for user : [{}]", signupDto.getUsername());
    ResponseEntity<Object> response = signup(signupDto);
    if (response.getStatusCode() == HttpStatus.OK) {
      return SUCCESS_HTML;
    } else {
      return ERROR_HTML;
    }
  }

  /**
   * processes a login request
   *
   * @param signupDto SignupDto
   * @return user Info
   */
  @Operation(summary = "Processes incoming signup request")
  @PostMapping( value = "/signup",
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseBody
  public ResponseEntity<Object> signup(@Valid @RequestBody SignupDto signupDto) {
    log.info("Received a login request for user : [{}]", signupDto.getUsername());
    String responseBody;
    try {
      userService.signupUser(signupDto);
      responseBody = ResponseUtil.createSuccessResponse(signupDto);
      return new ResponseEntity<>(responseBody, HttpStatus.OK);

    } catch (Exception e) {
      log.error("signup exception, data: {}, message:{}", signupDto, e);
      responseBody = ResponseUtil.createErrorResponse(NEW_USER_ERROR_MESSAGE,signupDto);
      return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(value = {"/loadData"})
  public String loadData(Model model) {
    try {
      DataUtils.loadData();
      return "success";

    } catch (Exception e) {
      System.out.println(e.getMessage());
      return e.getMessage();
    }

  }

}
