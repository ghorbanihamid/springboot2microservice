package com.soshiant.springbootexample.exception;

public class CustomerServiceException extends Exception {

  private static final long serialVersionUID = -1498536298137707867L;

  public CustomerServiceException() {
    super();
  }

  public CustomerServiceException(String message) {
    super(message);
  }

}
