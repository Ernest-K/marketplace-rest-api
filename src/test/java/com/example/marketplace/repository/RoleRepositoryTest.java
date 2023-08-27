package com.example.marketplace.repository;

import com.example.marketplace.model.Role;
import com.example.marketplace.model.RoleName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void Save_ValidRole_ReturnSavedRole() {
        Role role = Role.builder().name(RoleName.ROLE_USER).build();

        Role savedRole = roleRepository.save(role);

        assertThat(savedRole.getId()).isNotNull();
        assertThat(savedRole.getName()).isEqualTo(RoleName.ROLE_USER);
    }

    @Test
    public void FindAll_ReturnRoleList(){
        System.out.println(RoleName.values());

        Arrays.stream(RoleName.values())
                .map(roleName -> roleRepository.save(Role.builder().name(roleName).build()))
                .forEach(savedRole -> {});

        List<Role> roleList = roleRepository.findAll();

        assertThat(roleList).hasSize(RoleName.values().length);
    }

    @Test
    public void FindByName_ValidName_ReturnRole() {
        Role role = Role.builder().name(RoleName.ROLE_USER).build();
        roleRepository.save(role);

        Optional<Role> foundRole = roleRepository.findByName(RoleName.ROLE_USER);

        assertThat(foundRole).isPresent();
        assertThat(foundRole.get().getName()).isEqualTo(RoleName.ROLE_USER);
    }

    @Test
    public void DeleteById_ValidId_ReturnEmpty(){
        Role role = Role.builder().name(RoleName.ROLE_USER).build();
        Role savedRole = roleRepository.save(role);

        roleRepository.deleteById(savedRole.getId());

        Optional<Role> deletedRole = roleRepository.findById(savedRole.getId());

        assertThat(deletedRole).isEmpty();
    }
}
