package com.example.fintech.wallet.exception;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


@Hidden
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException ex, WebRequest request) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiError> handleAccountNotFound(AccountNotFoundException ex, WebRequest request) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EmailAlreadyRegisteredException.class, UsernameAlreadyRegisteredException.class})
    public ResponseEntity<ApiError> handleRegisterConflict(RuntimeException ex, WebRequest request) {
        ApiError error = new ApiError(HttpStatus.CONFLICT.value(), "Conflict", ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiError> handleInvalidCredentials(InvalidCredentialsException ex, WebRequest request) {
        ApiError error = new ApiError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InactiveUserException.class)
    public ResponseEntity<ApiError> handleInactiveUser(InactiveUserException ex, WebRequest request) {
        ApiError error = new ApiError(HttpStatus.FORBIDDEN.value(), "Forbidden", ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ApiError> handleInsufficientFunds(InsufficientFundsException ex, WebRequest request) {
        ApiError error = new ApiError(422, "Unprocessable Entity", ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TransactionNotAllowedException.class)
    public ResponseEntity<ApiError> handleTransactionNotAllowed(TransactionNotAllowedException ex, WebRequest request) {
        ApiError error = new ApiError(422, "Unprocessable Entity", ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(Exception.class)

    public ResponseEntity<ApiError> handleException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", "An unexpected error has occurred", request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
} 