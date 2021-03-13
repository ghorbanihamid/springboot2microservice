package com.soshiant.springbootexample.exception;

public class EmployeeServiceException extends Exception {

  private static final long serialVersionUID = 1422536298137707867L;

  public EmployeeServiceException() {
    super();
  }

  public EmployeeServiceException(String message) {
    super(message);
  }

}
