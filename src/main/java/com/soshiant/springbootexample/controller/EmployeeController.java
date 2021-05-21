package com.soshiant.springbootexample.controller;

import com.soshiant.springbootexample.dto.EmployeeRequestDto;
import com.soshiant.springbootexample.entity.EmployeeAddress;
import com.soshiant.springbootexample.service.EmployeeService;
import com.soshiant.springbootexample.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
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
public class EmployeeController {

  @Autowired
  private EmployeeService employeeService;

  /**
   * processes a received <b>register Employee</b> request
   *
   * @param employeeRequestDto employee info
   * @return ResponseEntity
   */
  @ApiOperation(value = "Processes incoming register employee")
  @PostMapping(value = "/register-employee",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public  ResponseEntity<Object> registerNewEmployee(@Valid @RequestBody EmployeeRequestDto employeeRequestDto) {
    log.info("Received a register-employee [{}]...", employeeRequestDto);

    try {
      EmployeeAddress savedEmployee = employeeService.registerEmployee(employeeRequestDto);

      if (savedEmployee == null) {
        log.error("couldn't register new employee {}", employeeRequestDto);
        return new ResponseEntity<>(
            ResponseUtil.createErrorResponse("couldn't register new employee!",null),
            HttpStatus.EXPECTATION_FAILED);
      }
      return new ResponseEntity<>(
          ResponseUtil.createSuccessResponse("employee create successfully.",savedEmployee.toString()),
          HttpStatus.OK);

    } catch (Exception e) {
      log.error("Error processing incoming 'register employee request'", e);
      return new ResponseEntity<>(
          ResponseUtil.createErrorResponse("couldn't register new employee!",null),
          HttpStatus.EXPECTATION_FAILED);
    }

  }

  /**
   * processes a received <b>get-employee</b> request
   *
   * @param employeeId employee Id
   * @return nothing
   */
  @ApiOperation(value = "Processes incoming get employee request")
  @GetMapping(value = "/employee-info",
      params ={"employee-id"},
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<Object> getEmployeeInfoByEmployeeId(
      @Valid @RequestParam(value = "employee-id") Long employeeId) {

    log.info("Received a get-employee [{}]...", employeeId);

    try {
      EmployeeRequestDto employeeInfo = employeeService.getEmployee(employeeId);

      if (employeeInfo == null) {
        log.error("couldn't get employee with Id {}", employeeId);
        return new ResponseEntity<>("{\"error\": \"employee not found\"}", HttpStatus.NOT_FOUND);
      }
      String response = ResponseUtil.getJsonString(employeeInfo);

      return new ResponseEntity<>(response, HttpStatus.OK);

    } catch (Exception e) {
      log.error("Error processing incoming 'get-employee'", e);
      return new ResponseEntity<>("{\"error\": \"System error\"}", HttpStatus.BAD_REQUEST);
    }
  }

}
