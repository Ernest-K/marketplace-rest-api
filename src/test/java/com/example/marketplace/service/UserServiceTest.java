package com.example.marketplace.service;

import com.example.marketplace.dto.request.CreateUserRequest;
import com.example.marketplace.dto.request.UpdateUserRequest;
import com.example.marketplace.dto.response.UserResponse;
import com.example.marketplace.exception.UserExistsException;
import com.example.marketplace.exception.UserNotFoundException;
import com.example.marketplace.mapper.UserMapper;
import com.example.marketplace.model.Role;
import com.example.marketplace.model.RoleName;
import com.example.marketplace.model.SecurityUser;
import com.example.marketplace.model.User;
import com.example.marketplace.repository.RoleRepository;
import com.example.marketplace.repository.UserRepository;
import com.example.marketplace.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;

    private User user1;
    private User user2;

    @BeforeEach
    public void init() {
        user1 = User.builder()
                .id(1L)
                .username("testUser1")
                .email("test1@example.com")
                .password("password")
                .roles(Set.of(new Role(RoleName.ROLE_USER)))
                .build();
        user2 = User.builder()
                .id(2L)
                .username("testUser2")
                .email("test2@example.com")
                .password("password")
                .roles(Set.of(new Role(RoleName.ROLE_USER)))
                .build();

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void GetUsers_ReturnUserResponseList() {
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        when(userMapper.mapToDto(Mockito.any(User.class))).thenReturn(new UserResponse());

        List<UserResponse> userResponseList = userService.getUsers();

        assertThat(userResponseList).isNotNull();
        assertThat(userResponseList).hasSize(2);
    }

    @Test
    public void GetUserById_ValidId_ReturnUserResponse() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        when(userMapper.mapToDto(Mockito.any(User.class))).thenReturn(new UserResponse());

        UserResponse userResponse = userService.getUserById(user1.getId());

        assertThat(userResponse).isNotNull();
    }

    @Test
    public void GetUserById_InvalidId_ThrowUserNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    public void CreateUser_CreateUserRequest_ReturnUserResponses() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("john_doe");
        createUserRequest.setEmail("john.doe@example.com");
        createUserRequest.setPassword("secretpassword");

        when(userRepository.existsByUsername("john_doe")).thenReturn(false);
        when(userRepository.existsByEmail("john.doe@example.com")).thenReturn(false);
        Role userRole = new Role();
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode("secretpassword")).thenReturn("hashedPassword");
        when(userMapper.mapToDto(Mockito.any(User.class))).thenReturn(new UserResponse());

        UserResponse userResponse = userService.createUser(createUserRequest);

        // Verify that userRepository.save was called
        verify(userRepository, times(1)).save(any(User.class));
        assertThat(userResponse).isNotNull();
    }

    @Test
    public void CreateUser_CreateUserRequestWithInvalidUsername_ThrowUserExistsException() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("existing_user");
        createUserRequest.setEmail("john.doe@example.com");
        createUserRequest.setPassword("secretpassword");

        when(userRepository.existsByUsername("existing_user")).thenReturn(true);

        assertThrows(UserExistsException.class, () -> userService.createUser(createUserRequest));
    }

    @Test
    public void CreateUser_CreateUserRequestWithInvalidEmail_ThrowUserExistsException() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("john_doe");
        createUserRequest.setEmail("existing_email@example.com");
        createUserRequest.setPassword("secretpassword");

        when(userRepository.existsByEmail("existing_email@example.com")).thenReturn(true);

        assertThrows(UserExistsException.class, () -> userService.createUser(createUserRequest));
    }

    @Test
    public void UpdateUser_UpdateUserRequest_ReturnUserResponse() {
        User user = User.builder()
                .id(1L)
                .username("john_doe")
                .email("john.doe@example.com")
                .password("secretpassword")
                .roles(Set.of(new Role(RoleName.ROLE_USER)))
                .build();

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setUsername("John");

        SecurityUser securityUser = new SecurityUser(user);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        when(userMapper.mapToDto(Mockito.any(User.class))).thenReturn(new UserResponse());

        UserResponse userResponse = userService.updateUser(user.getId(), updateUserRequest);

        verify(userRepository, times(1)).save(any(User.class));
        assertThat(userResponse).isNotNull();
    }

    @Test
    public void UpdateUser_UpdateUserRequest_ThrowAccessDeniedException() {
        User user = User.builder()
                .id(1L)
                .username("john_doe")
                .email("john.doe@example.com")
                .password("secretpassword")
                .roles(Set.of(new Role(RoleName.ROLE_USER)))
                .build();

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setUsername("John");

        SecurityUser securityUser = new SecurityUser(user);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(securityUser);

        assertThrows(AccessDeniedException.class, () -> userService.updateUser(2L, updateUserRequest));
    }

    @Test
    public void DeleteUser_ValidUserId_ReturnVoid() {
        User user = User.builder()
                .id(1L)
                .username("john_doe")
                .email("john.doe@example.com")
                .password("secretpassword")
                .roles(Set.of(new Role(RoleName.ROLE_USER)))
                .build();

        SecurityUser securityUser = new SecurityUser(user);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertAll(() -> userService.deleteUser(user.getId()));
    }

    @Test
    public void DeleteUser_InvalidUserId_ThrowAccessDeniedException() {
        User user = User.builder()
                .id(1L)
                .username("john_doe")
                .email("john.doe@example.com")
                .password("secretpassword")
                .roles(Set.of(new Role(RoleName.ROLE_USER)))
                .build();

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setUsername("John");

        SecurityUser securityUser = new SecurityUser(user);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(securityUser);

        assertThrows(AccessDeniedException.class, () -> userService.deleteUser(2L));
    }
}
