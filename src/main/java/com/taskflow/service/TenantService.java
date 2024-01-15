package com.taskflow.service;

import org.springframework.stereotype.Service;


@Service
public interface TenantService {
    void createTenant(String tenantName);
}
