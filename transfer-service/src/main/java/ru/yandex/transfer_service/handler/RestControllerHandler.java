package ru.yandex.transfer_service.handler;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class RestControllerHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ProblemDetail> handleRuntimeException(RuntimeException e) {
        log.error("Unhandled runtime exception ({}) handler: {}", e.getClass().getSimpleName(), e.getMessage(), e);
        return ResponseEntity
            .of(buildProblemDetail("Unhandled API error", HttpStatus.INTERNAL_SERVER_ERROR))
            .build();
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ProblemDetail> handleHttpClientErrorException(HttpClientErrorException ex) {
        return ResponseEntity
            .of(ex.getResponseBodyAs(ProblemDetail.class))
            .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, String> errorMap = new HashMap<>();
        e.getConstraintViolations().forEach(error -> errorMap.put(error.getPropertyPath().toString(), error.getMessage()));
        log.debug("ConstraintViolationException handler: {}", e.getConstraintViolations().stream().map(error -> "\n%s - %s".formatted(error.getPropertyPath().toString(), error.getMessage())).collect(Collectors.joining("; ")));
        return ResponseEntity
            .of(buildProblemDetail("Invalid request body", HttpStatus.BAD_REQUEST, Map.of("validation", errorMap)))
            .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDeniedException(AccessDeniedException e) {
        log.debug("AccessDeniedException handler: {}", e.getMessage());
        return ResponseEntity
            .of(buildProblemDetail(e.getMessage(), HttpStatus.FORBIDDEN))
            .build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ProblemDetail> handleAuthenticationException(AuthenticationException e) {
        log.debug("AuthenticationException: {}", e.getMessage());
        return ResponseEntity
            .of(buildProblemDetail(e.getMessage(), HttpStatus.UNAUTHORIZED))
            .build();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> errorMap.put(((FieldError) error).getField(), error.getDefaultMessage()));
        ProblemDetail body = buildProblemDetail("Invalid request parameters", HttpStatus.BAD_REQUEST, Map.of("validation", errorMap));
        return this.handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        log.debug("{} handler: {}; path: {}", ex.getClass().getSimpleName(), ex.getMessage());
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
      return this.handleExceptionInternal(ex, (Object)null, headers, status, request);
   }

    private ProblemDetail buildProblemDetail(String message, HttpStatusCode status) {
        return buildProblemDetail(message, status, null);
    }

    private ProblemDetail buildProblemDetail(String message, HttpStatusCode status, Map<String, Object> properties) {
        URI instance = URI.create(((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getRequestURI());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, message);
        problemDetail.setInstance(instance);
        problemDetail.setProperties(properties);
        return problemDetail;
    }
}
