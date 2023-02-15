package com.soshiant.springbootexample.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.soshiant.springbootexample.dto.LoginResponseDto;
import com.soshiant.springbootexample.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class ResponseUtil {

  public static final String SUCCESS = "SUCCESS";
  public static final String ERROR = "ERROR";

  private ResponseUtil() {

  }

  public static String createSuccessResponse(Object detail) {
    String token = "";
    return new ResponseDto(SUCCESS,"",JacksonUtils.toJson(detail),token)
      .toString();
  }

  public static String createErrorResponse(String message) {
    return createErrorResponse(message, null);
  }

  public static String createErrorResponse(String message,
                                           Object detail) {
    String token = "";
    return new ResponseDto(ERROR,message,JacksonUtils.toJson(detail),token)
      .toString();
  }


}
