package com.Mirra.eCommerce.Exception;

import com.Mirra.eCommerce.Exception.registration.UserNotFoundException;
import com.Mirra.eCommerce.Exception.registration.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public String handleValidationException(ValidationException ex) {
        // Handle the custom validation exception
        return "authentication-register"; // Return to the registration form with the error message.
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
