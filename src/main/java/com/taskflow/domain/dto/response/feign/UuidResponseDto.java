package com.taskflow.domain.dto.response.feign;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UuidResponseDto {
    private String uuid;
}
