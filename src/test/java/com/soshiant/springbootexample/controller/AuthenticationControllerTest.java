package com.soshiant.springbootexample.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.soshiant.springbootexample.dto.LoginResponseDto;
import com.soshiant.springbootexample.filter.AuthenticationFilter;
import com.soshiant.springbootexample.repository.UserRepository;
import com.soshiant.springbootexample.service.AuthenticationService;
import com.soshiant.springbootexample.service.UserService;
import com.soshiant.springbootexample.util.TestUtil;
import com.soshiant.springbootexample.util.ValidatorTestUtil;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc
class AuthenticationControllerTest {

  @MockBean
  private AuthenticationService authenticationService;

  @MockBean
  private UserService userService;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  DaoAuthenticationProvider authProvider;

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
  private AuthenticationController authenticationController;

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
    List<Object> servicesList = new ArrayList<>();
    servicesList.add(authenticationService);
    validator = ValidatorTestUtil.getCustomValidatorFactoryBean(servicesList,servletContext);
  }

  @BeforeEach
  void setUp() {
    ReflectionTestUtils
      .setField(authenticationController,"authenticationService",authenticationService);

    mockMvc = MockMvcBuilders
        .standaloneSetup(authenticationController)
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
  void login_Success() throws Exception {

    LoginResponseDto loginResponseDto = TestUtil.buildLoginResponseDto();
    when(authenticationService.authenticate(anyString(),anyString())).thenReturn(loginResponseDto);
    MvcResult result = mockMvc.perform(post("/authenticate/login")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .characterEncoding("utf-8")
        .content(TestUtil.buildLoginRequestDtoAsJson())
      )
      .andDo(print())
      .andExpect(status().isOk())
      .andReturn();

    assertThat(result, is(notNullValue()));
    String content = result.getResponse().getContentAsString();
    assertThat(content, is(notNullValue()));
    verify(authenticationService).authenticate(isA(String.class),isA(String.class));
  }
  @Test
  void login_without_user_info() throws Exception {

    mockMvc.perform(post("/authenticate/login")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .characterEncoding("utf-8")
        .content("")
      )
      .andDo(print())
      .andExpect(status().isBadRequest());
  }

  @Test
  void login_with_invalid_user_info() throws Exception {

    when(authenticationService.authenticate(anyString(),anyString())).thenReturn(null);
    mockMvc.perform(post("/authenticate/login")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .characterEncoding("utf-8")
        .content(TestUtil.buildLoginRequestDtoAsJson())
      )
      .andDo(print())
      .andExpect(status().isUnauthorized());
  }
  @Test
  void logout() throws Exception {

    when(authenticationService.logout(anyString())).thenReturn(true);
    mockMvc.perform(get("/authenticate/logout")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .characterEncoding("utf-8")
        .param("username","username")
      )
      .andDo(print())
      .andExpect(status().isOk());

    verify(authenticationService).logout(isA(String.class));
  }
}