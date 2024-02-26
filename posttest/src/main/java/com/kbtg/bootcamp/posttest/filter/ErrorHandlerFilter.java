package com.kbtg.bootcamp.posttest.filter;

import com.kbtg.bootcamp.posttest.dto.ErrorResponseDto;
import com.kbtg.bootcamp.posttest.exception.BusinessValidationException;
import com.kbtg.bootcamp.posttest.exception.PersistenceFailureException;
import com.kbtg.bootcamp.posttest.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;

import static com.kbtg.bootcamp.posttest.utils.ErrorMessage.VALIDATION_FAILED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
public class ErrorHandlerFilter extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var errors = new HashMap<>();
        for (var err : ex.getBindingResult().getAllErrors())
            errors.put(((FieldError) err).getField(), err.getDefaultMessage());

        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ErrorResponseDto response = new ErrorResponseDto(BAD_REQUEST.value(), LocalDateTime.now(), errors, VALIDATION_FAILED, path);
        return this.handleExceptionInternal(ex, response, headers, status, request);
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessValidationException(HttpServletRequest request, BusinessValidationException e) {
        ErrorResponseDto response = new ErrorResponseDto(BAD_REQUEST.value(), LocalDateTime.now(), BAD_REQUEST.getReasonPhrase(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(HttpServletRequest request, ResourceNotFoundException e) {
        ErrorResponseDto response = new ErrorResponseDto(NOT_FOUND.value(), LocalDateTime.now(), NOT_FOUND.getReasonPhrase(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(response, NOT_FOUND);
    }

    @ExceptionHandler(PersistenceFailureException.class)
    public ResponseEntity<ErrorResponseDto> handlePersistenceFailureException(HttpServletRequest request, PersistenceFailureException e) {
        ErrorResponseDto response = new ErrorResponseDto(INTERNAL_SERVER_ERROR.value(), LocalDateTime.now(), INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorResponseDto> handleRequestPathVariablesValidationException(HttpServletRequest request, ConstraintViolationException e) {
        var errors = new HashMap<>();
        for (var err : e.getConstraintViolations()) {
            err.getMessageTemplate();
            errors.put(err.getPropertyPath().toString().split("\\.")[1], err.getMessageTemplate());
        }

        ErrorResponseDto response = new ErrorResponseDto(BAD_REQUEST.value(),
                LocalDateTime.now(),
                errors,
                VALIDATION_FAILED,
                request.getRequestURI());

        return new ResponseEntity<>(response, BAD_REQUEST);
    }
}