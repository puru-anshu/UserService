package com.arutech.mftracker.UserService.controller;

import com.arutech.mftracker.UserService.dto.*;
import com.arutech.mftracker.UserService.exceptions.UserAlreadyExistsException;
import com.arutech.mftracker.UserService.exceptions.UserNotFoundException;
import com.arutech.mftracker.UserService.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<TokenResponseDto> signup(
            @Valid @RequestBody UserCredentialsDto credentials
    ) {
        try {
            return ResponseEntity.ok(authService.signup(credentials));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        }

    }

    @PostMapping("/signin")
    public ResponseEntity<TokenResponseDto> signin(
            @Valid @RequestBody UserCredentialsDto credentials
    ) {
        try {
            TokenResponseDto tokenResponseDto = authService.signin(credentials);
            log.info("Token Response: {}", tokenResponseDto);
            return ResponseEntity.ok(tokenResponseDto);
        } catch (UserNotFoundException uex) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException rex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/google-auth")
    public ResponseEntity<TokenResponseDto> googleAuth(
            @Valid @RequestBody GoogleAuthDto googleAuth
    ) {
        return ResponseEntity.ok(authService.googleSignin(googleAuth));
    }

    @PostMapping("/signout")
    public ResponseEntity<Void> signout() {
        authService.logout();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponseDto> refreshToken(
            @RequestHeader("Authorization") String refreshToken
    ) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

    @PostMapping("/password-reset/request")
    public ResponseEntity<Void> requestPasswordReset(
            @RequestBody PasswordResetRequestDto resetRequest
    ) {
        authService.initiatePasswordReset(resetRequest.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password-reset/confirm")
    public ResponseEntity<Void> confirmPasswordReset(
            @RequestBody PasswordResetConfirmDto resetConfirm
    ) {
        authService.confirmPasswordReset(
                resetConfirm.getResetToken(),
                resetConfirm.getNewPassword()
        );
        return ResponseEntity.ok().build();
    }
}
