package com.soshiant.springbootexample.util;


import com.soshiant.springbootexample.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;


@Slf4j
@ActiveProfiles("test")
@ExtendWith({SpringExtension.class, MockitoExtension.class})
class JacksonUtilsTest {


  @Test
  @DisplayName("Test toObject")
  void testToObject() {
    try {
      String JsonStr =
        "{ \n" +
        "  \"status\": \"SUCCESS\",                                              \n" +
        "  \"error\": \"\",                                                      \n" +
        "  \"token\":\"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJoYW1\",                   \n" +
        "  \"data\": {                                                           \n" +
        "              \"username\":\"hamid\",                                   \n" +
        "              \"first-name\":null,                                      \n" +
        "              \"last-name\":null,                                       \n" +
        "              \"phone-number\":null,                                    \n" +
        "              \"email-address\":null,                                   \n" +
        "              \"token\":\"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJoYW1\"        \n" +
        "            }                                                           \n" +
        "}";

      ResponseDto responseDto = (ResponseDto) JacksonUtils.toObject(JsonStr, ResponseDto.class);
      assertThat(responseDto, is(notNullValue()));

    } catch (Exception e) {
      throw new RuntimeException("Failed to convert Login object to JSON String!");
    }
  }

}
