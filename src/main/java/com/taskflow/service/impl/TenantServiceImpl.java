package com.taskflow.service.impl;
import com.taskflow.config.context.TenantContext;
import com.taskflow.domain.entity.Tenant;
import com.taskflow.repository.TenantRepository;
import com.taskflow.repository.UserRepository;
import com.taskflow.service.TenantService;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Statement;
import java.time.LocalDateTime;

@Component
public class TenantServiceImpl implements TenantService {
    private final DataSource dataSource;
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;

    public TenantServiceImpl
    (
        DataSource dataSource,
        TenantRepository tenantRepository,
        UserRepository userRepository) {
        this.dataSource = dataSource;
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
    }
    @Override
    public Boolean checkAvailableTenant(String tenantId) {
        return (! tenantRepository.checkAvailableTenant(tenantId) );
    }
    @Override
    @Transactional
    public void createTenant(Tenant tenant,String tenantId) {
        //Todo: look for an alternative way to create schemas using jpa
        try(Statement statement = dataSource.getConnection().createStatement()) {
            //create schema
            statement.execute("CREATE SCHEMA " + tenantId);
            // run liquibase changelogs on the new schema
            SpringLiquibase liquibase = new SpringLiquibase();
            liquibase.setDataSource(dataSource);
            liquibase.setDefaultSchema(tenantId);
            liquibase.setChangeLog("db/changelog/tenants/db.tenant.changelog-master.yml");
            liquibase.afterPropertiesSet();
            //insert tenant
            tenant.setAccessionDate(LocalDateTime.now());
            tenant.setIsActive(true);
            tenant.setTenantId(tenantId);
            tenantRepository.save(tenant);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
