package com.example.marketplace.mapper;

import com.example.marketplace.dto.response.UserResponse;
import com.example.marketplace.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper{

    public UserResponse mapToDto(User user){
        return new UserResponse(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone());
    }
}
