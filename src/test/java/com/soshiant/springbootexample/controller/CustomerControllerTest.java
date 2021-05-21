package com.soshiant.springbootexample.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.soshiant.springbootexample.dto.CustomerRequestDto;
import com.soshiant.springbootexample.entity.Customer;
import com.soshiant.springbootexample.filter.AuthenticationFilter;
import com.soshiant.springbootexample.service.CustomerService;
import com.soshiant.springbootexample.service.UserService;
import com.soshiant.springbootexample.util.TestUtil;
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
import org.powermock.api.mockito.PowerMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

  @MockBean
  private CustomerService customerService;

  @MockBean
  private UserService userService;

  @MockBean
  private AuthenticationFilter authenticationFilter;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webAppContext;

  private static Validator validator;

  @InjectMocks
  private CustomerController customerController;

  @BeforeAll
  public static void setupValidatorInstance() throws Exception {
    MockServletContext servletContext =  new MockServletContext();
    CustomerService customerService = mock(CustomerService.class);
    List<Object> servicesList = new ArrayList<>();
    servicesList.add(customerService);
    validator = ValidatorTestUtil.getCustomValidatorFactoryBean(servicesList,servletContext);
    PowerMockito.when(customerService.getCustomer(anyString())).thenReturn(null);
  }

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(customerController,"customerService",customerService);
    mockMvc = MockMvcBuilders
        .standaloneSetup(customerController)
        .setValidator(validator)
        .setHandlerExceptionResolvers()
        .build();
  }

  @AfterEach
  void tearDown() {
    validateMockitoUsage();
  }

  @Test
  @DisplayName("Test register customer request is Success")
  void testRegisterNewCustomerIsSuccess() throws Exception {

    Customer customer = TestUtil.buildCustomerObject();
    when(customerService.registerCustomer(any(CustomerRequestDto.class))).thenReturn(customer);

    mockMvc.perform(post("/register-customer")
        .content(TestUtil.buildCustomerDtoAsJson())
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());

    verify(customerService).registerCustomer(isA(CustomerRequestDto.class));
  }

  @Test
  @DisplayName("Test register customer request failed")
  void testRegisterNewCustomerIsFailed() throws Exception {

    when(customerService.registerCustomer(any(CustomerRequestDto.class))).thenReturn(null);

    mockMvc.perform(post("/register-customer")
        .content(TestUtil.buildCustomerDtoAsJson())
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isExpectationFailed());

    verify(customerService).registerCustomer(isA(CustomerRequestDto.class));
  }

}