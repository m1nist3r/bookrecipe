package com.portfolio.recipebook.util;

public class UserEmailAlreadyExistException extends RuntimeException {
    public UserEmailAlreadyExistException(String email) {
        super("User email: " + email + " already exist.");
    }
}
