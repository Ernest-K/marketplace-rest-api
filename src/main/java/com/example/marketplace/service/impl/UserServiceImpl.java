package com.example.marketplace.service.impl;

import com.example.marketplace.dto.UserResponse;
import com.example.marketplace.exception.UserNotFoundException;
import com.example.marketplace.mapper.UserMapper;
import com.example.marketplace.repository.UserRepository;
import com.example.marketplace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUser(Long id) {
        return userRepository.findById(id)
                .map(userMapper)
                .orElseThrow(()-> new UserNotFoundException(id.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper)
                .collect(Collectors.toList());
    }
}
