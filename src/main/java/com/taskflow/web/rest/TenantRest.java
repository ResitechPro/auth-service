package com.taskflow.web.rest;

import com.taskflow.service.TenantService;
import com.taskflow.utils.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tenants")
public class TenantRest {
    private final TenantService tenantService;
    public TenantRest(TenantService tenantService) {
        this.tenantService = tenantService;
    }
    @GetMapping("/check-tenant/{tenantId}")
    public ResponseEntity<Response<Boolean>> checkTenant(@PathVariable String tenantId) {
        Response<Boolean> response = new Response<>();
        Boolean isTenantAvailable = tenantService.checkAvailableTenant(tenantId);
        if(Boolean.TRUE.equals(isTenantAvailable)) response.setMessage("Tenant is available");
        else response.setMessage("Tenant is not available");
        response.setResult(
                isTenantAvailable
        );
        return ResponseEntity.ok(response);
    }
}
