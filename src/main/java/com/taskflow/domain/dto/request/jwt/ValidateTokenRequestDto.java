package com.taskflow.domain.dto.request.jwt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ValidateTokenRequestDto {
    @NotNull(message = "Token cannot be null")
    @NotBlank(message = "Token cannot be blank")
    private String accessToken;
}
