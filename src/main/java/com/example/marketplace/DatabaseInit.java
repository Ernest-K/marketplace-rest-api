package com.example.marketplace;

import com.example.marketplace.model.Category;
import com.example.marketplace.model.Offer;
import com.example.marketplace.model.User;
import com.example.marketplace.repository.CategoryRepository;
import com.example.marketplace.repository.OfferRepository;
import com.example.marketplace.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class DatabaseInit {

    private final UserRepository userRepository;
    private final OfferRepository offerRepository;
    private final CategoryRepository categoryRepository;

    @PostConstruct
    private void init(){
        User user = new User("John", "John123", "John@gmail.com");
        Category category = new Category("Automotive");
        Offer offer = new Offer("Volkswagen Golf", "Very fast", 2300.0, user, category);

        User user1 = new User("Anna", "Anna123", "anna@gmail.com");
        Category category1 = new Category("Electronics");
        Offer offer1 = new Offer("iPhone 12", "Brand new", 5000.0, user1, category1);

        User user2 = new User("Mark", "Mark123", "mark@gmail.com");
        Category category2 = new Category("Real Estate");
        Offer offer2 = new Offer("2-Bedroom Apartment", "Spacious and modern", 3000.0, user2, category2);

        User user3 = new User("Emily", "Emily123", "emily@gmail.com");
        Category category3 = new Category("Fashion");
        Offer offer3 = new Offer("Designer Handbag", "Limited edition", 2000.0, user3, category3);

        userRepository.save(user);
        categoryRepository.save(category);
        offerRepository.save(offer);

        userRepository.save(user1);
        categoryRepository.save(category1);
        offerRepository.save(offer1);

        userRepository.save(user2);
        categoryRepository.save(category2);
        offerRepository.save(offer2);

        userRepository.save(user3);
        categoryRepository.save(category3);
        offerRepository.save(offer3);
    }
}
