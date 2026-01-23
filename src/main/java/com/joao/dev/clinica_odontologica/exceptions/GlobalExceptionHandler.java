package com.joao.dev.clinica_odontologica.exceptions;

import com.joao.dev.clinica_odontologica.config.Constant;
import com.joao.dev.clinica_odontologica.dto.GlobalResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<GlobalResponse<Object>> handleBadRequestException(Exception e, HttpServletRequest req) {
        GlobalResponse<Object> res = GlobalResponse.failure(e.getMessage(), generateDetails(e, req));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<GlobalResponse<Object>> handleEntityAlreadyExistsException(Exception e, HttpServletRequest req) {
        GlobalResponse<Object> res = GlobalResponse.failure(e.getMessage(), generateDetails(e, req));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GlobalResponse<Object>> handleResourceNotFoundException(
            ResourceNotFoundException e, HttpServletRequest req) {
        GlobalResponse<Object> res = GlobalResponse.failure(e.getMessage(), generateDetails(e, req));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    // MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        GlobalResponse<Object> errorResponse = GlobalResponse.failure(message, generateDetails(ex, request));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // MethodArgumentMismatchException
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<GlobalResponse<Object>> handleTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String parameterName = ex.getName();
        String expectedType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
        String message = String.format(Constant.GR_ERROR_PARAMETER_TYPE, parameterName, expectedType);

        GlobalResponse<Object> errorResponse = GlobalResponse.failure(message, generateDetails(ex, request));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // NoHandlerFoundException
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<GlobalResponse<Object>> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request) {
        GlobalResponse<Object> errorResponse = GlobalResponse.failure(ex.getMessage(), generateDetails(ex, request));
        errorResponse.setMessage(Constant.GR_ERROR_NO_HANDLER);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // InvalidCredentialsException
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<GlobalResponse<Object>> handleInvalidCredentialsException(InvalidCredentialsException e, HttpServletRequest req) {
        GlobalResponse<Object> res = GlobalResponse.failure(e.getMessage(), generateDetails(e, req));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
    }


    // private
    private String generateDetails(Exception e, HttpServletRequest req) {
        return String.format(Constant.GR_ERROR_DETAILS, req.getMethod(), req.getRequestURI());
    }
}
