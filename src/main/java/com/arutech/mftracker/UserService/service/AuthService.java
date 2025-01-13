package com.arutech.mftracker.UserService.service;

import com.arutech.mftracker.UserService.config.JwtTokenProvider;
import com.arutech.mftracker.UserService.dto.*;
import com.arutech.mftracker.UserService.exceptions.InvalidTokenException;
import com.arutech.mftracker.UserService.exceptions.UserAlreadyExistsException;
import com.arutech.mftracker.UserService.exceptions.UserNotFoundException;
import com.arutech.mftracker.UserService.model.User;
import com.arutech.mftracker.UserService.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final GoogleAuthService googleAuthService;
    private final UserService userService;

    @Transactional
    public TokenResponseDto signup(UserCredentialsDto credentials) {
        // Check if user already exists
        if (userRepository.findByEmail(credentials.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already registered");
        }
        log.info("Signup request received for email: {}", credentials.toString());
        // Create new user
        User newUser = User.builder()
                .email(credentials.getEmail())
                .password(passwordEncoder.encode(credentials.getPassword()))
                .fullName(credentials.getUsername())
                .registrationDate(LocalDateTime.now())
                .authProvider(User.AuthProvider.LOCAL)
                .build();

        // Save user
        User savedUser = userRepository.save(newUser);

        // Generate tokens
        UserDetails userDetails = userService.loadUserByUsername(savedUser.getEmail());
        String accessToken = jwtTokenProvider.generateToken(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);


        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userProfile(convertToUserProfileDto(savedUser))
//                .issuedAt(LocalDateTime.now())
//                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .build();
    }

    @Transactional
    public TokenResponseDto signin(UserCredentialsDto credentials) {


        log.info("checking for user email {}", credentials.getEmail());
        // Find user
        User user = userRepository.findByEmail(credentials.getEmail().trim())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Match password
        if (!passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }
        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Generate tokens
        UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
        String accessToken = jwtTokenProvider.generateToken(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userProfile(convertToUserProfileDto(user))
                .build();


    }

    @Transactional
    public TokenResponseDto googleSignin(GoogleAuthDto googleAuth) {
        // Verify Google token
        GoogleUserInfoDto googleUserInfo = googleAuthService.verifyGoogleToken(googleAuth.getGoogleToken());

        // Find or create user
        User user = userRepository.findByEmail(googleUserInfo.getEmail())
                .map(existingUser -> {
                    // Update last login for existing user
                    existingUser.setLastLogin(LocalDateTime.now());
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    // Create new user for first-time Google login
                    User newUser = User.builder()
                            .email(googleUserInfo.getEmail())
                            .fullName(googleUserInfo.getName())
                            .profilePictureUrl(googleUserInfo.getPictureUrl())
                            .registrationDate(LocalDateTime.now())
                            .authProvider(User.AuthProvider.GOOGLE)
                            .googleSub(googleUserInfo.getSub())
                            .build();
                    return userRepository.save(newUser);
                });

        // Generate tokens
        UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
        String accessToken = jwtTokenProvider.generateToken(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userProfile(convertToUserProfileDto(user))
                .build();
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    @Transactional
    public TokenResponseDto refreshToken(String refreshToken) {
        // Remove "Bearer " prefix if present
        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }

        // Extract username from refresh token
        String username = jwtTokenProvider.extractUsername(refreshToken);

        // Find user
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Validate refresh token
        UserDetails userDetails = userService.loadUserByUsername(username);

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new InvalidTokenException("Invalid or expired refresh token");
        }

        // Generate new access and refresh tokens
        String newAccessToken = jwtTokenProvider.generateToken(userDetails);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

        return TokenResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .userProfile(convertToUserProfileDto(user))
                .build();
    }

    @Transactional
    public void initiatePasswordReset(String email) {
        // Find user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Generate and send password reset token (mock implementation)
        String resetToken = generatePasswordResetToken();

        // In a real implementation, you would:
        // 1. Save reset token in database with expiration
        // 2. Send email with reset link containing the token
        log.info("Password reset token generated for {}: {}", email, resetToken);
    }

    // Helper method to convert User to UserProfileDto
    private UserProfileDto convertToUserProfileDto(User user) {
        return UserProfileDto.builder()
                .userId(user.getId().toString())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .registrationDate(user.getRegistrationDate())
                .lastLogin(user.getLastLogin())
                .build();
    }

    // Generate a mock password reset token (replace with secure implementation)
    private String generatePasswordResetToken() {
        return UUID.randomUUID().toString();
    }

    public void confirmPasswordReset(String resetToken, String newPassword) {
        // In a real implementation, you would:
        // 1. Validate reset token and retrieve user
        // 2. Update user password in database

        log.info("Password reset confirmed for token: {}", resetToken);
    }
}
