package com.taskflow.web.feign;

import com.taskflow.domain.dto.request.tenant.TenantCreationRequestDto;
import com.taskflow.domain.dto.response.auth.JwtAuthenticationResponseDto;
import com.taskflow.utils.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "property-management-service")
public interface PropertyManagementClient {
    final String URL_PREFIX = "/api/v1";
    @PostMapping(URL_PREFIX + "/tenants")
    Response<JwtAuthenticationResponseDto> diffuseTenant(
            @RequestHeader("X-tenant-id") String tenantId,
            TenantCreationRequestDto tenantCreationRequestDto
    );
}
