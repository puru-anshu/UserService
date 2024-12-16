package com.arutech.mftracker.UserService.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordResetConfirmDto {
    private String resetToken;
    private String newPassword;
}
