package com.taskflow.repository;

import com.taskflow.domain.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    @Query("select count(t) > 0 from Tenant t where t.tenantId = :tenantId")
    Boolean checkAvailableTenant(String tenantId);
}