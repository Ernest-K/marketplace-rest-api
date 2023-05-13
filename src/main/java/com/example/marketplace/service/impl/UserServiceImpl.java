package com.example.marketplace.service.impl;

import com.example.marketplace.dto.UserResponse;
import com.example.marketplace.exception.UserNotFoundException;
import com.example.marketplace.mapper.UserMapper;
import com.example.marketplace.model.User;
import com.example.marketplace.repository.UserRepository;
import com.example.marketplace.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse get(Long id) {
        return userRepository.findById(id)
                .map(userMapper)
                .orElseThrow(()-> new UserNotFoundException(id.toString()));
    }
}
