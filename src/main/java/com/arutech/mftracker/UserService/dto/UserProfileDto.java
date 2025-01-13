package com.arutech.mftracker.UserService.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileDto {

    private String userId;
    private String username;
    private String email;
    private String fullName;
    private String profilePictureUrl;
    private String bio;
    private LocalDateTime registrationDate;
    private LocalDateTime lastLogin ;

}
