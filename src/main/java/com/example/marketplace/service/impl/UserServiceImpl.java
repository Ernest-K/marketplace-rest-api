    package com.example.marketplace.service.impl;

    import com.example.marketplace.dto.request.CreateUserRequest;
    import com.example.marketplace.dto.request.UpdateUserRequest;
    import com.example.marketplace.dto.response.UserResponse;
    import com.example.marketplace.exception.RoleNotFoundException;
    import com.example.marketplace.exception.UserExistsException;
    import com.example.marketplace.exception.UserNotFoundException;
    import com.example.marketplace.mapper.UserMapper;
    import com.example.marketplace.model.Role;
    import com.example.marketplace.model.RoleName;
    import com.example.marketplace.model.SecurityUser;
    import com.example.marketplace.model.User;
    import com.example.marketplace.repository.RoleRepository;
    import com.example.marketplace.repository.UserRepository;
    import com.example.marketplace.service.UserService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.security.access.AccessDeniedException;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.lang.reflect.Field;
    import java.util.Collections;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.stream.Collectors;

    @RequiredArgsConstructor
    @Service
    public class UserServiceImpl implements UserService {

        private final UserRepository userRepository;
        private final UserMapper userMapper;
        private final PasswordEncoder passwordEncoder;
        private final RoleRepository roleRepository;

        @Override
        @Transactional(readOnly = true)
        public List<UserResponse> getUsers() {
            return userRepository.findAll()
                    .stream()
                    .map(userMapper::mapToDto)
                    .collect(Collectors.toList());
        }
        @Override
        @Transactional(readOnly = true)
        public UserResponse getUserById(Long userId) {
            return userRepository.findById(userId)
                    .map(userMapper::mapToDto)
                    .orElseThrow(()-> new UserNotFoundException("No user with id: " + userId));
        }

        @Override
        @Transactional
        public UserResponse createUser(CreateUserRequest createUserRequest) {
            if (userRepository.existsByUsername(createUserRequest.getUsername())){
                throw new UserExistsException("User with name: " + createUserRequest.getUsername() + " already exists");
            }
            if (userRepository.existsByEmail(createUserRequest.getEmail())){
                throw new UserExistsException("User with email: " + createUserRequest.getEmail() + " already exists");
            }

            User user = new User();

            Map<String, Object> dtoMap = convertDtoToMap(createUserRequest);

            for (Map.Entry<String, Object> entry : dtoMap.entrySet()) {
                String fieldName = entry.getKey();
                Object newValue = entry.getValue();
                if (newValue != null){
                    if (fieldName.equals("password")){
                        updateFieldInEntity(user, fieldName, passwordEncoder.encode((String) newValue));
                    }else{
                        updateFieldInEntity(user, fieldName, newValue);
                    }
                }
            }

            Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new RoleNotFoundException("Role: " + RoleName.ROLE_USER + " not found"));
            user.setRoles(Collections.singleton(userRole));

            userRepository.save(user);

            return userMapper.mapToDto(user);
        }

        @Override
        @Transactional
        public UserResponse updateUser(Long userId, UpdateUserRequest updateUserRequest) {

            if(!hasUserAccess(userId)){
                throw new AccessDeniedException("Not allowed to update user");
            }

            User userToUpdate = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("No user with id: " + userId));

            Map<String, Object> dtoMap = convertDtoToMap(updateUserRequest);

            for (Map.Entry<String, Object> entry : dtoMap.entrySet()) {
                String fieldName = entry.getKey();
                Object newValue = entry.getValue();
                if (newValue != null){
                    if (fieldName.equals("password")){
                        updateFieldInEntity(userToUpdate, fieldName, passwordEncoder.encode((String) newValue));
                    }else{
                        updateFieldInEntity(userToUpdate, fieldName, newValue);
                    }
                }
            }

            return userMapper.mapToDto(userRepository.save(userToUpdate));
        }

        @Override
        @Transactional
        public void deleteUser(Long userId) {
            if (!hasUserAccess(userId)){
                throw new AccessDeniedException("Not allowed to delete");
            }

            User userToDelete = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("No user with id: " + userId));

            userRepository.delete(userToDelete);
        }

        private <T> Map<String, Object> convertDtoToMap(T userRequest){
            Map<String, Object> dtoMap = new HashMap<>();

            Field[] fields = userRequest.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = null;
                try {
                    value = field.get(userRequest);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                dtoMap.put(field.getName(), value);
            }

            return dtoMap;
        }

        private void updateFieldInEntity(User user, String fieldName, Object newValue) {
            try {
                Field field = user.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(user, newValue);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        private boolean hasUserAccess(Long userId){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            SecurityUser authenticatedSecurityUser = (SecurityUser) authentication.getPrincipal();

            return userId.equals(authenticatedSecurityUser.getId()) || authenticatedSecurityUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.name()));
        }
    }
