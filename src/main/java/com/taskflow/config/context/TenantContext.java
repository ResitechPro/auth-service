package com.taskflow.config.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
public class TenantContext {
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    private TenantContext() {
    }

    public static void setCurrentTenant(String tenant) {
        log.info("Setting tenant to " + tenant);
        currentTenant.set(tenant);
    }

    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    public static void clear() {
        currentTenant.remove();
    }
}
