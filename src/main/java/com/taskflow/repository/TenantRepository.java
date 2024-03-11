package com.taskflow.repository;

import com.taskflow.domain.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    @Query("select count(t) > 0 from Tenant t where t.tenantId = :tenantId")
    Boolean checkAvailableTenant(String tenantId);
    @Query("select t from Tenant t where t.organizationName = :organizationName")
    Optional<Tenant> findByOrganizationName(String organizationName);
    @Query("select t from Tenant t where t.personalEmail = :personalEmail")
    Optional<Tenant> findByPersonalEmail(String personalEmail);
}