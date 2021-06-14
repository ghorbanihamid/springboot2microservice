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

  public static String createLoginSuccessResponse(LoginResponseDto loginResponseDto) {

    return new ResponseDto(SUCCESS,"",getJsonString(loginResponseDto)).toString();
  }

  public static String createSuccessResponse(Object detail) {
    return new ResponseDto(SUCCESS,"",getJsonString(detail,false))
      .toString();
  }

  public static String createSuccessResponse(Object detail, boolean excludeNullFields) {
    return new ResponseDto(SUCCESS,"",getJsonString(detail,excludeNullFields))
      .toString();
  }

  public static String createErrorResponse(String message) {
    return createErrorResponse(message, null, false);
  }

  public static String createErrorResponse(String message, Object detail) {
    return createErrorResponse(message, detail, false);
  }

  public static String createErrorResponse(String message,
                                           Object detail,
                                           boolean excludeNullFields) {

    return new ResponseDto(ERROR,message,getJsonString(detail,excludeNullFields))
      .toString();
  }

  public static String getJsonString(Object obj) {
    return getJsonString(obj, false);
  }
  public static String getJsonString(Object obj,boolean excludeNullFields) {
    String objJson = "";
    try {
      if (obj != null) {
        ObjectMapper mapper = new ObjectMapper();
        // configure ObjectMapper to exclude null fields while serializing
        if(excludeNullFields) {
          mapper.setSerializationInclusion(Include.NON_NULL);
        }
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        objJson = new ObjectMapper().writeValueAsString(obj);
      }
      return objJson;
    } catch (Exception e) {
      log.error("Failed to convert input object to JSON String!" + obj.toString());
      return objJson;
    }
  }

  public static Object getObjectFromJsonString(String jsonString) {
    Object resultObject = null;
    try {
      if (StringUtils.isBlank(jsonString)) {
        return null;
      }
      resultObject = new ObjectMapper().readValue(jsonString, Object.class);

    } catch (Exception e) {
      log.error("Failed to convert input object to JSON String!" + jsonString.toString());
    }
    return resultObject;
  }
}
