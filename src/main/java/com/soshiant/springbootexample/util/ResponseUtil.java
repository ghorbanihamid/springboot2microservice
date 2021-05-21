package com.soshiant.springbootexample.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soshiant.springbootexample.dto.LoginResponseDto;
import com.soshiant.springbootexample.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResponseUtil {

  private ResponseUtil() {

  }

  public static String createLoginSuccessResponse(LoginResponseDto loginResponseDto) {

    return new ResponseDto("SUCCESS","",loginResponseDto).toString();
  }

  public static String createSuccessResponse(String message, String detail) {
    return new ResponseDto("SUCCESS",message,detail).toString();
  }

  public static String createErrorResponse(String message, String detail) {
    return new ResponseDto("ERROR",message,detail).toString();
  }

  public static String getJsonString(Object obj) {

    try {
      return new ObjectMapper().writeValueAsString(obj);

    } catch (Exception e) {
      log.error("Failed to convert input object to JSON String!" + obj.toString());
      return obj.toString();
    }
  }
}
