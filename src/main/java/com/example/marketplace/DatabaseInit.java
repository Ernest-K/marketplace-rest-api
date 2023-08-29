package com.example.marketplace;

import com.example.marketplace.model.*;
import com.example.marketplace.repository.CategoryRepository;
import com.example.marketplace.repository.OfferRepository;
import com.example.marketplace.repository.RoleRepository;
import com.example.marketplace.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;


@RequiredArgsConstructor
@Component
public class DatabaseInit {

    private final UserRepository userRepository;
    private final OfferRepository offerRepository;
    private final CategoryRepository categoryRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    private void init(){

        Role adminRole = roleRepository.save(new Role(RoleName.ROLE_ADMIN));
        Role userRole = roleRepository.save(new Role(RoleName.ROLE_USER));

        User admin = new User("admin", passwordEncoder.encode("admin"), "admin@gmail.com");
        admin.setRoles(List.of(adminRole, userRole));

        User user = new User("John", passwordEncoder.encode("John123"), "John@gmail.com");
        user.setRoles(Collections.singletonList(userRole));
        Category category = new Category("Automotive");
        Offer offer = new Offer("Volkswagen Golf", "Very fast", 2300.0, user, category);

        User user1 = new User("Anna", passwordEncoder.encode("Anna123"), "anna@gmail.com");
        user1.setRoles(Collections.singletonList(userRole));
        Category category1 = new Category("Electronics");
        Offer offer1 = new Offer("iPhone 12", "Brand new", 5000.0, user1, category1);

        User user2 = new User("Mark", passwordEncoder.encode("Mark123"), "mark@gmail.com");
        user2.setRoles(Collections.singletonList(userRole));
        Category category2 = new Category("Real Estate");
        Offer offer2 = new Offer("2-Bedroom Apartment", "Spacious and modern", 3000.0, user2, category2);

        User user3 = new User("Emily", passwordEncoder.encode("Emily123"), "emily@gmail.com");
        user3.setRoles(Collections.singletonList(userRole));
        Category category3 = new Category("Fashion");
        Offer offer3 = new Offer("Designer Handbag", "Limited edition", 2000.0, user3, category3);

        userRepository.save(admin);

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
