package com.taskflow.service;
import com.taskflow.repository.TenantRepository;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Statement;

@Component
public class TenantServiceImpl implements TenantService {
    private final DataSource dataSource;
    private final TenantRepository tenantRepository;

    public TenantServiceImpl
    (
        DataSource dataSource,
        TenantRepository tenantRepository
    ) {
        this.dataSource = dataSource;
        this.tenantRepository = tenantRepository;
    }

    @Override
    public Boolean checkAvailableTenant(String tenantName) {
        return (! tenantRepository.checkAvailableTenant(tenantName) );
    }

    @Override
    @Transactional
    public void createTenant(String tenantName) {
        //Todo: look for an alternative way to create schemas using jpa
        try(Statement statement = dataSource.getConnection().createStatement()) {
            statement.execute("CREATE SCHEMA " + tenantName);
        } catch (Exception e) {
            throw new RuntimeException("Organization already exists");
        }

        // working based on schema
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setDefaultSchema(tenantName);
        liquibase.setChangeLog("db/changelog/tenants/db.tenant.changelog-master.yml");
        try {
            liquibase.afterPropertiesSet();
        } catch (LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }
}
