package com.taskflow.service;

import org.springframework.stereotype.Service;

import javax.swing.text.StyledEditorKit;


@Service
public interface TenantService {
    Boolean checkAvailableTenant(String tenantName);
    void createTenant(String tenantName);
}
