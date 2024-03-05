package com.taskflow.service;

import com.taskflow.domain.entity.Tenant;
import org.springframework.stereotype.Service;



@Service
public interface TenantService {
    Boolean checkAvailableTenant(String tenantId);
    void createTenant(Tenant tenant, String tenantId);
}
