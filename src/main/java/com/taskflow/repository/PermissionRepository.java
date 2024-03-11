package com.taskflow.repository;

import com.taskflow.domain.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findBySubjectAndAction(String subject, String action);
}
