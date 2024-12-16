package com.arutech.mftracker.UserService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GoogleAuthDto {
    private String googleToken;
    private String email;
    private String firstName;
    private String lastName;

}
