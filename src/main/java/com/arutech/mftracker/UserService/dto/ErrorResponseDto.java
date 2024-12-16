package com.arutech.mftracker.UserService.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class ErrorResponseDto {
    private String message;
    private String errorCode;
}
