package com.arutech.mftracker.UserService.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class GoogleUserInfoDto {
    private String sub;
    private String email;
    private String name;
    private String pictureUrl;
}
