package com.soshiant.springbootexample.util;

public class AppConstants {

  private AppConstants() {
  }

  public static final Integer SOCKET_TIMEOUT                  = 150000;
  public static final Integer CONNECTION_TIMEOUT              = 3000;

  public static final String XML_DECLARATION                  ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n";

  public static final String HTTP_URL                         = "http://";
  public static final String HTTPS_URL                        = "https://";

  public static final String LOCAL_HOST                       = "http://localhost";

  public static final String RESOURCES                         = "/src/main/resources/";
  public static final String REDIRECT                          = "redirect:";

  public static final String JAVA_EXTENSION                    = ".java";
  public static final String CLASS_EXTENSION                   = ".class";

  public static final String BIRTH_DATE_FORMAT                 = "yyyy-MM-dd";
  public static final String DATE_TIME_FORMAT                  = "yyyy-MM-dd HH:mm:ss.SSS";
  public static final String PACIFIC_TIME_ZONE                 = "America/Los_Angeles";

  public static final String RESPONSE_TYPE_EMPTY               = "EMPTY";
  public static final String RESPONSE_TYPE_MALFORMED           = "MALFORMED";
  public static final String RESPONSE_TYPE_UNEXPECTED_ELEMENT  = "UNEXPECTED_ELEMENT";

  public static final String NORMAL                            = "Normal";
  public static final String EMPTY                             = "Empty";
  public static final String MALFORMED                         = "Malformed";
  public static final String UNEXPECTED_ELEMENT                = "Unexpected Element";
  public static final String MESSAGE                           = "message";

  public static final String LOGIN_HTML                        = "login.html";
  public static final String SUCCESS_HTML                      = "success.html";
  public static final String ERROR_HTML                        = "error.html";
  public static final String DASHBOARD_HTML                    = "dashboard.html";
  public static final String INDEX_HTML                        = "index.html";
  public static final String AUTHORIZATION                     = "Authorization";
  public static final String BEARER                            = "Bearer ";

  public static final String KEY_SEPARATOR_EQUAL               = "=";
  public static final String KEY_SEPARATOR_DASH                = "-";
  public static final String NONE                              = "NONE";

  public static final String ERROR_CODE_FILE_PREFIX            = "classpath:errorcodes/";
  public static final String ERROR_CODE_FILE_POSTFIX           = "-errorcodes.properties";

  public static final String CORE                              = "core";
  public static final String DOT                               = ".";

  public static final String TOKEN_STATUS_ENUM                 = "TokenStatusEnum";
  public static final String PAYMENT_STATUS_ENUM               = "PaymentStatusEnum";
  public static final String RECONCILE_STATUS_ENUM             = "ReconcileStatusEnum";
  public static final String ENUM_FOLDER_NAME                  = "enums";
  public static final String DOMAIN_FOLDER_NAME                = "domain";
  public static final String SERVICE_FOLDER_NAME               = "service";
  public static final String SERVICE_IMPL_FOLDER_NAME          = "service.impl";
  public static final String CORE_TOKEN_SERVICE_IMPL           = "ProcessorTokenSvcImpl";

  public static final String PROCESSOR_FOLDER_NAME             = "processors";
  public static final String PROCESS_REQUEST                   = "processRequest";
  public static final String GET_3D_REQUEST_AUTH_DATA          = "get3DRequestAuthenticationData";
  public static final String PROCESS_3D_REQUEST                = "process3DRequest";
  public static final String TOKEN                             = "token";
  public static final String ERROR                             = "error";
  public static final String PAYMENT                           = "payment";
  public static final String RECONCILE                         = "reconcile";
  public static final String PROCESS_TOKEN_REQUEST             = "processTokenRequest";
  public static final String VBV_3D_PAYMENT                    = "3DSecurePayment";
  public static final String PROCESS_PAYMENT_REQUEST           = "processPaymentRequest";
  public static final String PROCESS_3D_PAYMENT_REQUEST        = "process3DPaymentRequest";
  public static final String STATUS_CHECK                      = "statuscheck";
  public static final String PROCESS_STATUS_CHECK_REQUEST      = "processStatusCheckRequest";

  public static final String VISA_CARD_PREFIX                  = "4";
  public static final String MASTER_CARD_PREFIX                = "5";
  public static final String DISCOVER_PREFIX                   = "6";
  public static final String AMERICAN_EXPRESS_PREFIX           = "";

  public static final String VISA                              = "VISA";
  public static final String MASTER                            = "MASTERCARD";
  public static final String DISCOVER                          = "DISCOVER";
  public static final String AMERICAN_EXPRESS                  = "AMERICAN EXPRESS";

  public static final String PROCESSOR_QUOTATION               = "processor '";

  public static final String INTERNAL_SERVER_ERROR_MESSAGE      = "Internal server error!, please contact customer service.";

  public static final String LOGIN_INVALID_USER_ERROR_MESSAGE   = "Invalid username or password!";

  public static final String LOGIN_USER_NOT_FOUND_ERROR_MESSAGE = "Invalid username or password!";

  public static final String LOGIN_USER_DISABLED_ERROR_MESSAGE  = "User is disabled!";

  public static final String LOGIN_USER_LOCKED_ERROR_MESSAGE    = "User is locked!";

  public static final String NEW_USER_ERROR_MESSAGE             = "couldn't register new user!";

  public static final String NEW_CUSTOMER_ERROR_MESSAGE         = "couldn't register new customer!";

  public static final Long   EMPLOYEE_ID       = 123456L;
  public static final Long   CUSTOMER_ID       = 123456L;
  public static final String PHONE_NUMBER      = "7786362222";
  public static final String EMAIL_ADDRESS     = "customer_email@mail.com";
  public static final String FIRST_NAME        = "Hubert";
  public static final String LAST_NAME         = "Lopez";
  public static final String COUNTRY           = "United States";
  public static final String STATE             = "California";
  public static final String CITY              = "Los Angeles";
  public static final String STREET_NUMBER     = "1807";
  public static final String STREET_NAME       = "Brown Blvd";
  public static final String ZIP_CODE          = "23616";

}
