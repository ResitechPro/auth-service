package com.taskflow.domain.dto.request.tenant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TenantCreationRequestDto {
    private String userId;
    private String firstName;
    private String lastName;
    private String personalEmail;
    private String email;
    private String organizationName;
    private String organizationId;
    private String password;
    private String phone;
}
