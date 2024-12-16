package com.arutech.mftracker.UserService.exceptions;

public class GoogleAuthenticationException extends RuntimeException{
    public GoogleAuthenticationException(String message) {
        super(message);
    }

    public GoogleAuthenticationException(String googleAuthenticationFailed, Exception e) {
        super(googleAuthenticationFailed, e);
    }
}
