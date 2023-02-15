package com.soshiant.springbootexample.controller;

import com.soshiant.springbootexample.dto.CustomerRequestDto;
import com.soshiant.springbootexample.dto.CustomerUpdateDto;
import com.soshiant.springbootexample.dto.SignupDto;
import com.soshiant.springbootexample.entity.Customer;
import com.soshiant.springbootexample.exception.CustomerServiceException;
import com.soshiant.springbootexample.service.CustomerService;
import com.soshiant.springbootexample.util.JacksonUtils;
import com.soshiant.springbootexample.util.ResponseUtil;
import com.soshiant.springbootexample.validation.CustomerIdValidation;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Email;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.soshiant.springbootexample.util.AppConstants.*;
import static com.soshiant.springbootexample.util.AppConstants.ERROR_HTML;

/**
 *
 * @author Hamid.Ghorbani
 */

@Slf4j
@Controller
@Validated
@RequestMapping("/customer")
public class CustomerController {

  @Autowired
  private CustomerService customerService;

  @RequestMapping(value = {"/register-page"}, method = RequestMethod.GET)
  public String showRegisterNewCustomerPage(Model model){
    model.addAttribute("customerRequestDto", new CustomerRequestDto());
    return "customer-register";
  }

  /**
   * processes a customer register request
   *
   * @param customerRequestDto CustomerRequestDto
   * @return success
   */
  @Operation(summary = "Processes incoming customer register request")
  @PostMapping( value = "/register",
          consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE
  )
  public String registerNewCustomer(@Valid @ModelAttribute("customerRequestDto") CustomerRequestDto customerRequestDto, Model model) {
    log.info("Received a customer register request for user : [{}]", customerRequestDto);
    ResponseEntity<Object> response = registerNewCustomer(customerRequestDto);
    if (response.getStatusCode() == HttpStatus.OK) {
      return SUCCESS_HTML;
    } else {
      return ERROR_HTML;
    }
  }

  /**
   * processes a received <b>register-customer</b> request
   *
   * @param customerRequestDto customer info
   * @return nothing
   */
  @Operation(summary  = "Processes incoming register customer")
  @PostMapping(value = "/register",
               consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<Object> registerNewCustomer(@Valid @RequestBody CustomerRequestDto customerRequestDto) {
    log.info("Received a register-customer [{}]...", customerRequestDto);
    String responseBody = "";
    try {
      Customer savedCustomer = customerService.registerCustomer(customerRequestDto);

      if (savedCustomer == null) {
        log.error("couldn't register new customer {}", customerRequestDto);
        throw new CustomerServiceException(NEW_CUSTOMER_ERROR_MESSAGE);
      }
      responseBody = ResponseUtil.createSuccessResponse(savedCustomer);
      return new ResponseEntity<>(responseBody, HttpStatus.OK);

    } catch (CustomerServiceException e) {
      log.error("Error processing incoming register-customer request {}, exception message:{}",
          customerRequestDto, e);
      responseBody = ResponseUtil.createErrorResponse(
          NEW_CUSTOMER_ERROR_MESSAGE + " " + e.getMessage());

      return new ResponseEntity<>(responseBody, HttpStatus.EXPECTATION_FAILED);

    } catch (Exception e) {
      log.error("Error processing incoming register-customer request {}, exception message:{}",
          customerRequestDto, e);
      return new ResponseEntity<>(INTERNAL_SERVER_ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * processes a received <b>update-customers-emailAddress</b> request
   *
   * @param customerId customer Id
   * @param emailAddress email address
   * @return ResponseEntity
   */
  @Operation(summary  = "updating customer's email address")
  @PreAuthorize("hasRole('ROLE_USER')")
  @PostMapping(value = "/update-email",
               produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<Object> updateCustomerEmailAddress(
              @Valid @CustomerIdValidation @RequestParam(value = "customer-id") Long customerId,
              @Valid @Email @RequestParam(value = "email-address") String emailAddress) {

    log.info("Received a updateCustomerEmailAddress [customerId:{}, emailAddress:{}]...",
             customerId, emailAddress);

    try {
      boolean updateResult = customerService.updateCustomerEmailAddress(customerId,
                                                                  emailAddress);

      if (!updateResult) {
        log.error("couldn't update customer {} 's email address {}", customerId, emailAddress);
        return new ResponseEntity<>(
            ResponseUtil.createErrorResponse("couldn't update customer's email address",
                "{ CustomerId : " + customerId + "emailAddress : " + emailAddress + "}"
            ),
            HttpStatus.EXPECTATION_FAILED);
      }
      return new ResponseEntity<>(
          ResponseUtil.createSuccessResponse("{ CustomerId : " + customerId +
                    "emailAddress : " + emailAddress + "}"
              ),
          HttpStatus.OK);

    } catch (Exception e) {
      log.error("Error updating customer [{}] 's email address, exception message {} ",customerId, e);
      return new ResponseEntity<>(
          ResponseUtil.createErrorResponse("couldn't update customer's email address",
              "{ CustomerId : " + customerId +
                    "emailAddress : " + emailAddress + "}"
          ),
          HttpStatus.EXPECTATION_FAILED);
    }
  }

  /**
   * processes a received <b>update-customer</b> request
   *
   * @param customerDto customer info
   * @return nothing
   */
  @Operation(summary = "Processes incoming update-customer request")
  @PreAuthorize("hasRole('ROLE_USER')")
  @PostMapping(value = "/update",
               consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseBody
  public ResponseEntity<Object> updateCustomerInfo(@Valid @RequestBody CustomerUpdateDto customerDto) {

    log.info("Received a updateCustomerInfo [{}]...", customerDto);
    try {
      Customer savedCustomer = customerService.updateCustomerInfo(customerDto);

      if (savedCustomer == null) {
        log.error("couldn't update customer info {}", customerDto);
        return new ResponseEntity<>(
            ResponseUtil.createErrorResponse("couldn't update customer info!",
                "params : " + customerDto),
            HttpStatus.EXPECTATION_FAILED);
      }

      return new ResponseEntity<>(
          ResponseUtil.createSuccessResponse(savedCustomer), HttpStatus.OK);

    } catch (Exception e) {
      log.error("Error processing incoming updateCustomerInfo {} , exception message {} ",customerDto, e);
      return new ResponseEntity<>(
          ResponseUtil.createErrorResponse(
              "couldn't update customer info :" + e.getMessage(),
              customerDto),
          HttpStatus.EXPECTATION_FAILED);
    }
  }

  /**
   * processes a received <b>customer-info</b> request
   *
   * @param customerIds customer Ids
   * @return List of customers
   */
  @Operation(summary = "Processes customer-ids and returns customer-list")
  @GetMapping(value = "/customer-info",
              params ={"customer-ids"},
              produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<Object> getCustomerListByCustomerIds(
      @Valid @RequestParam(value = "customer-ids", required = false) List<Long> customerIds) {

    log.info("Received a get-customer [{}]...", customerIds);

    try {
      List<Customer> customerList = customerService.getCustomers(customerIds);

      if (customerList.isEmpty()) {
        log.error("couldn't get customer with Ids {}", customerIds);
        return new ResponseEntity<>("{\"error\": \"customers not found\"}", HttpStatus.NOT_FOUND);
      }
      String response = JacksonUtils.toJson(customerList);

      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      log.error("Error processing incoming 'getCustomerListByCustomerIds', exception message {} ", e.getMessage());
      return new ResponseEntity<>("{\"error\": \"System error\"}", HttpStatus.BAD_REQUEST);
    }
  }

  /**
   *
   * @return All customers list
   */
  @Operation(summary = "returns all customers list")
  @GetMapping(value = "/customer-info", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<Object> getAllCustomers() {

    log.info("Received a get all customers ...");

    try {
      List<Customer> customerList = customerService.getCustomers(null);

      if (customerList.isEmpty()) {
        log.error("empty table");
        return new ResponseEntity<>("{\"error\": \"customers not found\"}", HttpStatus.NOT_FOUND);
      }
      String response = JacksonUtils.toJson(customerList);
      return new ResponseEntity<>(response, HttpStatus.OK);

    } catch (Exception e) {
      log.error("Error processing incoming getAllCustomers, exception message {} ", e.getMessage());
      return new ResponseEntity<>("{\"error\": \"System error\"}", HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * processes a received <b>get-customer</b> request
   *
   * @param firstName first-name
   * @param lastName last-name
   * @return customer-info
   */
  @Operation(summary = "Processes customer-name and returns customer-info")
  @GetMapping(value = "/customer-info",
              params ={"first-name","last-name"},
              produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<Object> getCustomerInfoByCustomerName(
                            @Valid @RequestParam("first-name") String firstName,
                            @Valid @RequestParam("last-name") String lastName) {
    String name = firstName + " " + lastName;
    log.info("Received a getCustomerInfoByCustomerName [{}]...", name);

    try {
      Customer customerInfo = customerService.getCustomer(firstName,lastName);
      if (customerInfo == null) {
        log.error("couldn't get customer with name {}", firstName + " " + lastName);
        return new ResponseEntity<>("{\"error\": \"customer not found\"}", HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(customerInfo, HttpStatus.OK);

    } catch (CustomerServiceException e) {
      log.error("Error processing incoming getCustomerInfoByCustomerId {}, exception message {} ",name, e.getMessage());
      return new ResponseEntity<>("System error", HttpStatus.BAD_REQUEST);

    } catch (Exception e) {
      log.error("Error processing incoming getCustomerInfoByCustomerId {}, exception message {} ",name, e);
      return new ResponseEntity<>("System error", HttpStatus.BAD_REQUEST);
    }

  }

}
