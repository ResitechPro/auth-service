package com.taskflow.service;

import com.taskflow.domain.entity.Tenant;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public interface TenantService {
    Boolean checkAvailableTenant(String tenantId);
    void createTenant(Tenant tenant, String tenantId);

    Optional<Tenant> getTenantByOrganizationName(String organizationName);
    Optional<Tenant> getTenantByPersonalEmail(String personalEmail);
}
