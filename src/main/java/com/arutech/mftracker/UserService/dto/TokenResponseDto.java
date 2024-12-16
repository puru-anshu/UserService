package com.arutech.mftracker.UserService.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSerialize
public class TokenResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Access token cannot be blank")
    @Pattern(regexp = "^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$",
            message = "Invalid access token format")
    private String accessToken;

    @NotBlank(message = "Refresh token cannot be blank")
    @Pattern(regexp = "^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$",
            message = "Invalid refresh token format")
    private String refreshToken;
//
////    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
//    @JsonIgnore
//    private LocalDateTime issuedAt;
//
////    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
//    @JsonIgnore
//    private LocalDateTime expiresAt;

    @NotNull(message = "User profile is required")
    private UserProfileDto userProfile;

    @Builder.Default
    private TokenType tokenType = TokenType.BEARER;

    // Additional metadata for token management
    private Long accessTokenExpiresIn;
    private Long refreshTokenExpiresIn;

    // Enum for token type
    public enum TokenType {
        BEARER, JWT
    }


    // Override toString to mask tokens
    @Override
    public String toString() {
        return "TokenResponseDto{" +
                "accessToken='****'" +
                ", refreshToken='****'" +
                ", userProfile=" + userProfile +
                ", tokenType=" + tokenType +
                '}';
    }

}