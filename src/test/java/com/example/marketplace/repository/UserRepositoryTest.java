package com.example.marketplace.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import com.example.marketplace.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void FindAll_ReturnUserList(){
        User user1 = User.builder().username("testUser1").email("test1@example.com").build();
        User user2 = User.builder().username("testUser2").email("test2@example.com").build();

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> userList = userRepository.findAll();

        assertThat(userList).hasSize(2).contains(user1, user2);
    }

    @Test
    public void Save_ValidUser_ReturnSavedUser() {
        User user = User.builder().username("testUser").email("test@example.com").build();

        userRepository.save(user);

        assertThat(user.getId()).isNotNull();
        assertThat(user.getUsername()).isEqualTo("testUser");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void FindById_ValidId_ReturnUser() {
        User user = User.builder().username("testUser").email("test@example.com").build();
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(user.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get()).isEqualTo(user);
    }

    @Test
    public void FindById_InvalidId_ReturnEmpty() {
        Optional<User> foundUser = userRepository.findById(1L);

        assertThat(foundUser).isEmpty();
    }

    @Test
    public void FindByUsername_ValidUsername_ReturnUser() {
        User user = User.builder().username("testUser").email("test@example.com").build();
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername("testUser");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get()).isEqualTo(user);
    }

    @Test
    public void FindByUsername_InvalidUsername_ReturnEmpty() {
        Optional<User> foundUser = userRepository.findByUsername("testUser");

        assertThat(foundUser).isEmpty();
    }

    @Test
    public void ExistsByUsername_ValidUsername_ReturnTrue() {
        User user = User.builder().username("testUser").email("test@example.com").build();
        userRepository.save(user);

        Boolean exists = userRepository.existsByUsername("testUser");

        assertThat(exists).isTrue();
    }

    @Test
    public void ExistsByUsername_InvalidUsername_ReturnFalse() {
        Boolean exists = userRepository.existsByUsername("testUser");

        assertThat(exists).isFalse();
    }

    @Test
    public void ExistsByEmail_ValidEmail_ReturnTrue() {
        User user = User.builder().username("testUser").email("test@example.com").build();
        userRepository.save(user);

        Boolean exists = userRepository.existsByEmail("test@example.com");

        assertThat(exists).isTrue();
    }

    @Test
    public void ExistsByEmail_InvalidEmail_ReturnTrue() {
        Boolean exists = userRepository.existsByEmail("test@example.com");

        assertThat(exists).isFalse();
    }

    @Test
    public void UpdateUser_ReturnUpdatedUser(){
        User user = User.builder().username("testUser").email("test@example.com").build();
        userRepository.save(user);

        User userToUpdate = userRepository.findById(user.getId()).get();

        userToUpdate.setUsername("updateUser");
        userToUpdate.setEmail("update@example.com");

        User updatedUser =  userRepository.save(userToUpdate);

        assertThat(updatedUser.getId()).isEqualTo(user.getId());
        assertThat(updatedUser.getUsername()).isEqualTo("updateUser");
        assertThat(updatedUser.getEmail()).isEqualTo("update@example.com");
    }

    @Test
    public void DeleteById_ValidId_ReturnEmpty(){
        User user = User.builder().username("testUser").email("test@example.com").build();
        userRepository.save(user);

        userRepository.deleteById(user.getId());

        Optional<User> deletedUser = userRepository.findById(user.getId());

        assertThat(deletedUser).isEmpty();
    }
}