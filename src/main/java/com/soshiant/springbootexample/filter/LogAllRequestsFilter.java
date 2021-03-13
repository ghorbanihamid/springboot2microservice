package com.soshiant.springbootexample.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A filter which logs all requests.
 *
 */

@Slf4j
@Component
public class LogAllRequestsFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

    try {
      filterChain.doFilter(wrappedRequest, response);
    } finally {
      logRequest(wrappedRequest);
    }

  }

  /**
   * extract the request body from cashed request
   * @param wrappedRequest request
   */
  @SuppressWarnings("unchecked")
  private void logRequest(ContentCachingRequestWrapper wrappedRequest) {

    StringBuilder logStr =
        new StringBuilder(System.lineSeparator())
            .append("=============== new request from emp ===============")
            .append(System.lineSeparator());
    logStr.append(" request url : ").append(wrappedRequest.getRequestURL()).append(",").append(System.lineSeparator());
    logStr.append(" request method : ").append(wrappedRequest.getMethod()).append(",").append(System.lineSeparator());
    logStr.append(" client address : ").append(wrappedRequest.getRemoteAddr()).append(",").append(System.lineSeparator());

    Map<String, String[]> params = wrappedRequest.getParameterMap();
    if (params.size() > 0) {
      logStr.append(" parameters : { ");
      params.forEach((key, values) -> logStr.append(key).append(" -> ").append(Arrays.toString(values)).append(", "));
      logStr.append(" },").append(System.lineSeparator());
    }

    Map<String, String> pathVariables = (Map<String, String>) wrappedRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    if (pathVariables != null && pathVariables.size() > 0) {
      logStr.append(" pathVariables : { ");
      pathVariables.forEach((k, v) -> logStr.append(k).append(" -> ").append(v).append(","));
      logStr.append(" },").append(System.lineSeparator());
    }

    String headers = Collections.list(wrappedRequest.getHeaderNames()).stream()
        .map(headerName -> headerName + " : " + Collections.list(wrappedRequest.getHeaders(headerName))).collect(Collectors.joining(", "));

    if (headers.isEmpty()) {
      logStr.append(System.lineSeparator()).append(" Request headers: NONE,");
    } else {
      logStr.append(System.lineSeparator()).append(" Request headers: { ").append(headers).append(" },")
            .append(System.lineSeparator());
    }

    String body = getBody(wrappedRequest);
    logStr.append(" request body : ").append(body).append(System.lineSeparator());
    logStr.append("=========================================================").append(System.lineSeparator());
    log.info("{}", logStr);

  }

  /**
   * extract the request body from cashed request
   * @param requestWrapper request
   * @return the request body as json
   */
  private String getBody(ContentCachingRequestWrapper requestWrapper) {

    String payload = "";
    try {
      if (requestWrapper == null) {
        return  "[error, unable get native request. ]";
      }
      byte[] buf = requestWrapper.getContentAsByteArray();
      if (buf.length == 0) {
        return "[empty body]";
      }
      payload = new String(buf, 0, buf.length, requestWrapper.getCharacterEncoding());

    } catch (Exception e) {
      log.info("getBody(), exception occurred during getting request body: {} ", e.getMessage());
    }
    return payload;
  }

}
