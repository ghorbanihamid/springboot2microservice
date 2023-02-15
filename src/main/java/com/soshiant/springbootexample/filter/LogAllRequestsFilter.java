package com.soshiant.springbootexample.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

/**
 * A filter which logs all emp requests.
 *
 */

@Slf4j
@Component
public class LogAllRequestsFilter extends OncePerRequestFilter {

    @Value("${logging.requests.css.js:false}")
    private boolean logCssAndJsRequests;

    private static final String[] filterValues = {"webjars","css","jpg","js"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
            if (Arrays.stream(filterValues).noneMatch(request.getRequestURI()::contains) ||
                    logCssAndJsRequests) {
                logRequestAndResponse(wrappedRequest, wrappedResponse);
            }

        } finally {
            wrappedResponse.copyBodyToResponse();
        }

    }

    /**
     * extract the request body from cashed request
     * @param wrappedRequest request
     */
    @SuppressWarnings("unchecked")
    private void logRequestAndResponse(ContentCachingRequestWrapper wrappedRequest,
                                       ContentCachingResponseWrapper responseWrapper) {

        logRequest(wrappedRequest);
        logResponse(responseWrapper);
        StringBuilder logStr = new StringBuilder(System.lineSeparator())
                .append("====================================================================================")
                .append(logRequest(wrappedRequest))
                .append(logResponse(responseWrapper))
                .append("====================================================================================");
        log.info("{}", logStr);
    }

    /**
     * extract the request body from cashed request
     * @param wrappedRequest request
     */
    @SuppressWarnings("unchecked")
    private String logRequest(ContentCachingRequestWrapper wrappedRequest) {

        StringBuilder requestLogStr =
                new StringBuilder(System.lineSeparator())
                        .append("======= request =======")
                        .append(System.lineSeparator());

        requestLogStr.append(" request url : ")
                .append(wrappedRequest.getRequestURL())
                .append(",").append(System.lineSeparator());

        requestLogStr.append(" request method : ")
                .append(wrappedRequest.getMethod())
                .append(",").append(System.lineSeparator());

        requestLogStr.append(" client address : ")
                .append(wrappedRequest.getRemoteAddr())
                .append(",").append(System.lineSeparator());

        Map<String, String[]> params = wrappedRequest.getParameterMap();
        if (params.size() > 0) {
            requestLogStr.append(" parameters : [");
            params.forEach((key, values) -> requestLogStr.append(key).append(" -> ")
                    .append(Arrays.toString(values))
                    .append(", "));
            requestLogStr.append("]").append(System.lineSeparator());
        }

        Map<String, String> pathVariables =
                (Map<String, String>) wrappedRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (pathVariables != null && pathVariables.size() > 0) {
            requestLogStr.append(" pathVariables : [");
            pathVariables.forEach((k, v) -> requestLogStr.append(k).append(" -> ").append(v).append(","));
            requestLogStr.append("]").append(System.lineSeparator());
        }

        String headers = Collections.list(wrappedRequest.getHeaderNames()).stream()
                .map(headerName -> headerName + " : " + Collections.list(wrappedRequest.getHeaders(headerName)))
                .collect(Collectors.joining(", "));

        if (headers.isEmpty()) {
            requestLogStr.append(System.lineSeparator()).append(" Request headers: [NONE]");
        } else {
            requestLogStr.append(System.lineSeparator()).append(" Request headers:[")
                    .append(headers).append("]")
                    .append(System.lineSeparator());
        }

        String body = getContent(wrappedRequest.getContentAsByteArray(),
                wrappedRequest.getCharacterEncoding());
        requestLogStr.append(" request body : ").append(body).append(System.lineSeparator());
        return requestLogStr.toString();
    }

    /**
     * extract the response body from cashed request
     * @param wrappedResponse ContentCachingResponseWrapper
     */
    @SuppressWarnings("unchecked")
    private String logResponse(ContentCachingResponseWrapper wrappedResponse) {

        StringBuilder responseLogStr =
                new StringBuilder(System.lineSeparator())
                        .append("======== response =========")
                        .append(System.lineSeparator());

        responseLogStr.append(" response Status : ")
                .append(wrappedResponse.getStatus())
                .append(",")
                .append(System.lineSeparator());

        StringBuilder headers = new StringBuilder();
        wrappedResponse.getHeaderNames()
                .forEach(headerName ->
                        wrappedResponse.getHeaders(headerName)
                                .forEach(headerValue ->
                                        headers.append(headerName)
                                                .append(" : ")
                                                .append(headerValue)
                                                .append(Collectors.joining(", "))
                                )
                );
        if (headers.length() == 0) {
            responseLogStr.append(System.lineSeparator())
                    .append(" response headers: [NONE],")
                    .append(System.lineSeparator());
        } else {
            responseLogStr.append(System.lineSeparator()).append(" response headers: [ ")
                    .append(headers)
                    .append(" ],")
                    .append(System.lineSeparator());
        }

        String body = getContent(wrappedResponse.getContentAsByteArray(),
                wrappedResponse.getCharacterEncoding());

        responseLogStr.append(" response body : ").append(body).append(System.lineSeparator());
        responseLogStr.append("=========================")
                .append(System.lineSeparator());

        return responseLogStr.toString();
    }

    /**
     * extract the body from cashed request or response
     * @param content byte[]
     * @param characterEncoding String
     * @return the request body as json
     */
    private String getContent(byte[] content,String characterEncoding ) {

        String payload = "";
        try {
            if (content == null || content.length == 0) {
                return "[empty body]";
            }
            payload = new String(content, 0, content.length, characterEncoding);

        } catch (UnsupportedEncodingException e) {
            log.info("getContent(), [Unsupported Encoding]");

        } catch (Exception e) {
            log.info("getContent(), exception [{}]", e.getMessage());
        }
        return payload;
    }

}
