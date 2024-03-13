package com.taskflow.repository;

import com.taskflow.domain.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    Optional<Permission> findBySubjectAndAction(String subject, String action);
}
