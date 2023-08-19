package com.example.marketplace.mapper;

import com.example.marketplace.dto.response.UserResponse;
import com.example.marketplace.model.User;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserMapper implements Function<User, UserResponse> {

    @Override
    public UserResponse apply(User user) {
        return new UserResponse(user.getId(),
                user.getUsername(),
                user.getEmail());
    }
}
