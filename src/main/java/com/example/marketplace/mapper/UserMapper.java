package com.example.marketplace.mapper;

import com.example.marketplace.dto.OfferResponse;
import com.example.marketplace.dto.UserResponse;
import com.example.marketplace.model.User;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class UserMapper implements Function<User, UserResponse> {

    @Override
    public UserResponse apply(User user) {
        return new UserResponse(user.getUsername(),
                user.getEmail(),
                user.getOffers().stream().map(offer ->
                        new OfferResponse(offer.getName(),
                                offer.getDescription(),
                                offer.getPrice(),
                                offer.getUser().getId(),
                                offer.getCategory()))
                        .collect(Collectors.toList()));
    }
}
