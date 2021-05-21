package com.soshiant.springbootexample.controller;

import static com.soshiant.springbootexample.util.AppConstants.INTERNAL_SERVER_ERROR_MESSAGE;
import static com.soshiant.springbootexample.util.AppConstants.NEW_CUSTOMER_ERROR_MESSAGE;

import com.soshiant.springbootexample.dto.CustomerRequestDto;
import com.soshiant.springbootexample.dto.CustomerUpdateDto;
import com.soshiant.springbootexample.entity.Customer;
import com.soshiant.springbootexample.exception.CustomerServiceException;
import com.soshiant.springbootexample.service.CustomerService;
import com.soshiant.springbootexample.util.ResponseUtil;
import com.soshiant.springbootexample.validation.CustomerIdValidation;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Hamid.Ghorbani
 */

@Slf4j
@Controller
@Validated
public class CustomerController {

  @Autowired
  private CustomerService customerService;

  /**
   * processes a received <b>register-customer</b> request
   *
   * @param customerRequestDto customer info
   * @return nothing
   */
  @ApiOperation(value = "Processes incoming register customer")
  @PostMapping(value = "/register-customer",
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
      String customerIdStr = "{\"customerId\":\"" + savedCustomer.getCustomerId() +"\"";
      responseBody = ResponseUtil.createSuccessResponse("",customerIdStr);
      return new ResponseEntity<>(responseBody, HttpStatus.OK);

    } catch (CustomerServiceException e) {
      log.error("Error processing incoming register-customer request {}, exception message:{}",
          customerRequestDto, e);
      responseBody = ResponseUtil.createErrorResponse(NEW_CUSTOMER_ERROR_MESSAGE,"");
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
  @ApiOperation(value = "updating customer's email address")
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
          ResponseUtil.createSuccessResponse("customer's email updated successfully.",
              "{ CustomerId : " + customerId +
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
  @ApiOperation(value = "Processes incoming update-customer request")
  @PostMapping(value = "/update-customer",
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
          ResponseUtil.createSuccessResponse("customer info updated successfully.",
              savedCustomer.toString()),
          HttpStatus.OK);

    } catch (Exception e) {
      log.error("Error processing incoming updateCustomerInfo {} , exception message {} ",customerDto, e);
      return new ResponseEntity<>(
          ResponseUtil.createErrorResponse("couldn't update customer info!",
              "params : " + customerDto),
          HttpStatus.EXPECTATION_FAILED);
    }
  }

  /**
   * processes a received <b>customer-info</b> request
   *
   * @param customerIds customer Ids
   * @return List of customers
   */
  @ApiOperation(value = "Processes customer-ids and returns customer-list")
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
      String response = ResponseUtil.getJsonString(customerList);

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
  @ApiOperation(value = "returns all customers list")
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
      String response = ResponseUtil.getJsonString(customerList);
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
  @ApiOperation(value = "Processes customer-name and returns customer-info")
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
