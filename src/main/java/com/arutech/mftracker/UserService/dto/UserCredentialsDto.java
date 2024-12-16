package com.arutech.mftracker.UserService.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserCredentialsDto {
    private String email;
    private String password;
    private String username;

}
