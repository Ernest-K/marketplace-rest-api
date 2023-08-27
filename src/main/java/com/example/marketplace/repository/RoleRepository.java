package com.example.marketplace.repository;

import com.example.marketplace.model.Role;
import com.example.marketplace.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName roleName);
}
