package com.soshiant.springbootexample.exception;

public class DataNotFoundException extends Exception {

  private static final long serialVersionUID = -1498536238137707867L;

  public DataNotFoundException() {
    super();
  }

  public DataNotFoundException(String message) {
    super(message);
  }

}
