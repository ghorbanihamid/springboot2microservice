package com.soshiant.springbootexample.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.soshiant.springbootexample.dto.EmployeeRequestDto;
import com.soshiant.springbootexample.entity.EmployeeAddress;
import com.soshiant.springbootexample.filter.AuthenticationFilter;
import com.soshiant.springbootexample.service.AuthenticationService;
import com.soshiant.springbootexample.service.EmployeeService;
import com.soshiant.springbootexample.util.DataUtils;
import com.soshiant.springbootexample.util.LocalDateSerializer;
import com.soshiant.springbootexample.util.ResponseUtil;
import com.soshiant.springbootexample.util.ValidatorTestUtil;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@WebMvcTest(EmployeeControllerTest.class)
@AutoConfigureMockMvc
class EmployeeControllerTest {

  public static final String REGISTER_EMPLOYEE_URL = "/employee/register";

  @MockBean
  private EmployeeService employeeService;

  @Autowired
  private AuthenticationFilter authenticationFilter;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webAppContext;

  @Autowired
  FilterChainProxy springSecurityFilterChain;

  private static Validator validator;

  @InjectMocks
  private EmployeeController employeeController;

  /*
   *  JUnit 5 @BeforeAll annotation is replacement of @BeforeClass annotation in JUnit 4.
   * It is used to signal that the annotated method should be executed before all tests in
   * the current test class.
   * the lifecycle mode is 'PER_METHOD' by default, so  @BeforeAll annotated method MUST
   * be a static method otherwise it will throw runtime error.
   * if we want to use non-static method, we should add following annotation on class
   *          @TestInstance(TestInstance.Lifecycle.PER_CLASS)
   */
  @BeforeAll
  public static void setupValidatorInstance() throws Exception {
    MockServletContext servletContext =  new MockServletContext();
    AuthenticationService authenticationService = mock(AuthenticationService.class);
    EmployeeService employeeService = mock(EmployeeService.class);
    List<Object> servicesList = new ArrayList<>();
    servicesList.add(employeeService);
    servicesList.add(authenticationService);
    validator = ValidatorTestUtil.getCustomValidatorFactoryBean(servicesList,servletContext);
  }

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(employeeController,"employeeService",employeeService);

    mockMvc = MockMvcBuilders
        .standaloneSetup(employeeController)
        .addFilters(springSecurityFilterChain)
        .setValidator(validator)
        .setHandlerExceptionResolvers()
        .build();
  }

  @AfterEach
  void tearDown() {
    validateMockitoUsage();
  }

  @Test
  @DisplayName("Test register employee request is Success")
  void testRegisterNewEmployeeIsSuccess() throws Exception {

    EmployeeAddress employeeInfo = DataUtils.buildEmployeeAddressObject();
    when(employeeService.registerEmployee(any(EmployeeRequestDto.class))).thenReturn(employeeInfo);

    MvcResult result = mockMvc.perform(post(REGISTER_EMPLOYEE_URL)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .characterEncoding("utf-8")
        .content(DataUtils.buildEmployeeDtoAsJson())
      )
      .andDo(print())
      .andExpect(status().isOk())
      .andReturn();

    assertThat(result, is(notNullValue()));
    String content = result.getResponse().getContentAsString();
    assertThat(content, is(notNullValue()));
    assertThat(content, containsString(ResponseUtil.SUCCESS));
    verify(employeeService).registerEmployee(isA(EmployeeRequestDto.class));
  }

  @Test
  @DisplayName("Test register employee request failed")
  void testRegisterNewEmployeeIsFailed() throws Exception {

    when(employeeService.registerEmployee(any(EmployeeRequestDto.class))).thenReturn(null);

    MvcResult result = mockMvc.perform(post(REGISTER_EMPLOYEE_URL)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .characterEncoding("utf-8")
        .content(DataUtils.buildEmployeeDtoAsJson())
      )
      .andDo(print())
      .andExpect(status().isExpectationFailed())
      .andReturn();

    assertThat(result, is(notNullValue()));
    String content = result.getResponse().getContentAsString();
    assertThat(content, is(notNullValue()));
    assertThat(content, containsString(ResponseUtil.ERROR));
    verify(employeeService).registerEmployee(isA(EmployeeRequestDto.class));
  }
}