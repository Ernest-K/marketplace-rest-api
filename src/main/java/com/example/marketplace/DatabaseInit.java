package com.example.marketplace;

import com.example.marketplace.model.Category;
import com.example.marketplace.model.Offer;
import com.example.marketplace.model.User;
import com.example.marketplace.repository.CategoryRepository;
import com.example.marketplace.repository.OfferRepository;
import com.example.marketplace.repository.UserRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatabaseInit {

    private final UserRepository userRepository;
    private final OfferRepository offerRepository;
    private final CategoryRepository categoryRepository;

    public DatabaseInit(UserRepository userRepository, OfferRepository offerRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.offerRepository = offerRepository;
        this.categoryRepository = categoryRepository;
    }

    @PostConstruct
    private void init(){
        User user = new User("John", "John123", "John@gmail.com");
        Category category = new Category("Automotive");
        Offer offer = new Offer("Volkswagen Golf", "Very fast", 2300.0, user, category);

        userRepository.save(user);
        categoryRepository.save(category);
        offerRepository.save(offer);
    }
}
