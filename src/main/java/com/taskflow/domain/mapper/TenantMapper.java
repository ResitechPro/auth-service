package com.taskflow.domain.mapper;

import com.taskflow.domain.entity.Tenant;
import com.taskflow.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring")
public interface TenantMapper {
    TenantMapper INSTANCE = Mappers.getMapper(TenantMapper.class);
    Tenant userToTenant(User user);
}
