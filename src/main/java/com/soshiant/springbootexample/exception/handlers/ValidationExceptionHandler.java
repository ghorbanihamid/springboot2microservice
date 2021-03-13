package com.soshiant.springbootexample.exception.handlers;

import com.soshiant.springbootexample.util.ResponseUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * *** we can use the following annotation instead of extending ResponseEntityExceptionHandler
   * *** @ExceptionHandler(value = {MethodArgumentNotValidException.class})
   *
   * <p>Handle exception thrown when request body is invalid. Creating custom error message based on
   * exception
   *
   * @return the specific sbe response with errors populated
   */
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      final MethodArgumentNotValidException ex,
      final HttpHeaders headers,
      final HttpStatus status,
      final WebRequest request) {

    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult().getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    ex.getBindingResult().getGlobalErrors()
        .forEach(error -> errors.put(error.getObjectName(), error.getDefaultMessage()));

    String response =
        ResponseUtil.createErrorResponse("Argument not valid!", errors.toString());

    log.error("handleMethodArgumentNotValidException for request {}, errors : {}",
        request.toString(),
        errors.toString());
    return ResponseEntity.badRequest().body(response);
  }

  /**
   * *** we can use the following annotation instead of extending ResponseEntityExceptionHandler
   * *** @ExceptionHandler(value = {HttpMessageNotReadableException.class})
   *
   * <p>Handle exception thrown when request body is invalid. Creating custom error message based on
   * exception
   *
   * @return the specific sbe response with errors populated
   */
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {

    Map<String, String> errors = new HashMap<>();
    Throwable mostSpecificCause = ex.getMostSpecificCause();
    errors.put(mostSpecificCause.getClass().getName(), mostSpecificCause.getMessage());

    String response =
        ResponseUtil.createErrorResponse("Invalid request body!", errors.toString());

    log.error("handleHttpMessageNotReadable for request {}, errors : {}",
        request.toString(), errors.toString());
    return ResponseEntity.badRequest().body(response);
  }

  /**
   * Handle exception thrown when request header invalid Creating custom error message based on
   * exception
   *
   * @param ex the exception thrown
   * @return the specific sbe response with errors populated
   */
  @SuppressWarnings("unchecked")
  @ExceptionHandler(value = {MissingRequestHeaderException.class})
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<Object> handleMissingRequestHeaderException(
      final MissingRequestHeaderException ex, final WebRequest request) {

    String errorMessage = String.format(
            "Invalid request header %s: missing value.", ex.getParameter().getParameterName());

    String response =
        ResponseUtil.createErrorResponse("Invalid request header!", errorMessage);

    log.error("handleMissingRequestHeaderException for request: {}, errors : {}",
        request.toString(), errorMessage);

    return ResponseEntity.badRequest().body(response);
  }

  /**
   * Handle exception thrown when request path variable is invalid Creating custom error message
   * based on exception
   *
   * @param ex the exception thrown
   */
  @SuppressWarnings("unchecked")
  @ExceptionHandler(value = {ConstraintViolationException.class})
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<Object> handleConstraintViolationException(
      final ConstraintViolationException ex, final WebRequest request) {

    Map<String, String> errors = new HashMap<>();
    ex.getConstraintViolations()
        .forEach(error ->
                errors.put(error.getRootBeanClass().getName() + " " + error.getPropertyPath(),
                           error.getMessage()));

    String response =
        ResponseUtil.createErrorResponse("Constraint violation!", errors.toString());

    log.error(
        "handleConstraintViolationException for request: {}, errors : {}",
        request.toString(),
        errors.toString());
    return ResponseEntity.badRequest().body(response);
  }

  /**
   * Handle exception thrown when given path variable is in wrong format Creating custom error
   * message based on exception
   *
   * @param ex the exception thrown
   * @return the specific sbe response with errors populated
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(
      final MethodArgumentTypeMismatchException ex, final WebRequest request) {

    String message = "Parameter '" + ex.getParameter().getParameterName() + "' must be '"
          + Objects.requireNonNull(ex.getRequiredType()).getSimpleName() + "'";

    String response = ResponseUtil.createErrorResponse("Invalid parameter type!", message);

    log.error("handleMethodArgumentTypeMismatchException for request: {}, violations : {}",
        request.toString(), message);
    return ResponseEntity.badRequest().body(response);
  }

//  @ExceptionHandler({
//      HttpMediaTypeNotSupportedException.class,
//      HttpMediaTypeNotAcceptableException.class,
//  })
//  public ResponseEntity<Object> handleMediaTypeExceptions(
//      final Exception ex, final WebRequest request ) {
//
//    String response = "";
//    try{
//
//      log.error("HttpMediaTypeNotSupportedException for request: {}, violations : {}",
//          request.toString(), ex.getMessage());
//
//      if (ex instanceof HttpMediaTypeNotSupportedException) {
//        response = ResponseUtil.createErrorResponse("Unsupported MediaType!", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);
//      }
//      else if (ex instanceof HttpMediaTypeNotAcceptableException) {
//        response = ResponseUtil.createErrorResponse("MediaType not acceptable!", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
//      }
//
//      response = ResponseUtil.createErrorResponse("Exception occurred!", ex.getMessage());
//      return ResponseEntity.badRequest().body(response);
//
//    } catch (Exception e){
//      response = ResponseUtil.createErrorResponse("Exception occurred!", e.getMessage());
//      return ResponseEntity.badRequest().body(response);
//    }
//  }

}
