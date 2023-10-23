package com.Mirra.eCommerce.Exception.registration;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
