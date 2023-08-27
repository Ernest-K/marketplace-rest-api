package com.example.marketplace.repository;

import com.example.marketplace.dto.response.OfferCount;
import com.example.marketplace.model.Category;
import com.example.marketplace.model.Offer;
import com.example.marketplace.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OfferRepositoryTest {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void Save_ValidOffer_ReturnSavedOffer() {
        User user = User.builder().username("testUser").email("test@example.com").build();
        Category category = Category.builder().name("Electronics").build();
        Offer offer = Offer.builder().name("Laptop").description("Great device").price(300.0).user(user).category(category).build();

        Offer savedOffer = offerRepository.save(offer);

        assertThat(savedOffer.getId()).isEqualTo(savedOffer.getId());
        assertThat(savedOffer.getName()).isEqualTo("Laptop");
        assertThat(savedOffer.getDescription()).isEqualTo("Great device");
        assertThat(savedOffer.getPrice()).isEqualTo(300.0);
        assertThat(savedOffer.getUser().getUsername()).isEqualTo("testUser");
        assertThat(savedOffer.getUser().getEmail()).isEqualTo("test@example.com");
        assertThat(savedOffer.getCategory().getName()).isEqualTo("Electronics");
    }

    @Test
    public void FindAll_ReturnOfferList(){
        Offer offer1 = Offer.builder().name("Laptop").description("Great device").price(300.0).build();
        Offer offer2 = Offer.builder().name("Book").description("Interesting literature").price(10.5).build();

        offerRepository.save(offer1);
        offerRepository.save(offer2);

        List<Offer> offerList = offerRepository.findAll();

        assertThat(offerList).hasSize(2).contains(offer1, offer2);
    }

    @Test
    public void FindById_ValidId_ReturnOffer() {
        Offer offer = Offer.builder().name("Laptop").description("Great device").price(300.0).build();
        Offer savedOffer = offerRepository.save(offer);

        Optional<Offer> foundOffer = offerRepository.findById(savedOffer.getId());

        assertThat(foundOffer).isPresent();
        assertThat(foundOffer.get()).isEqualTo(offer);
    }

    @Test
    public void FindById_InvalidId_ReturnEmpty() {
        Optional<Offer> foundOffer = offerRepository.findById(1L);

        assertThat(foundOffer).isEmpty();
    }

    @Test
    public void FindAllByCategoryId_ValidId_ReturnOfferPage() {
        Category category = Category.builder().name("Electronics").build();
        categoryRepository.save(category);

        Offer offer = Offer.builder().name("Laptop").category(category).build();
        offerRepository.save(offer);

        Page<Offer> offersByCategory = offerRepository.findAllByCategoryId(category.getId(), Pageable.unpaged());

        assertThat(offersByCategory.getContent()).contains(offer);
    }

    @Test
    public void FindAllByUserId_ValidId_ReturnOfferPage() {
        User user = User.builder().username("testUser").build();
        userRepository.save(user);

        Offer offer = Offer.builder().name("Laptop").user(user).build();
        offerRepository.save(offer);

        Page<Offer> offersByCategory = offerRepository.findAllByUserId(user.getId(), Pageable.unpaged());

        assertThat(offersByCategory.getContent()).contains(offer);
    }

    @Test
    public void countTotalOffersByCategory_ReturnOfferCountList() {
        Category electronics = Category.builder().name("Electronics").build();
        Category books = Category.builder().name("Books").build();
        categoryRepository.saveAll(List.of(electronics, books));

        Offer offer1 = Offer.builder().name("Laptop").category(electronics).build();
        Offer offer2 = Offer.builder().name("Phone").category(electronics).build();
        Offer offer3 = Offer.builder().name("Novel").category(books).build();
        offerRepository.saveAll(List.of(offer1, offer2, offer3));

        List<OfferCount> offerCounts = offerRepository.countTotalOffersByCategory();

        assertThat(offerCounts).hasSize(2);

        OfferCount electronicsCount = offerCounts.stream()
                .filter(count -> count.getCategoryName().equals("Electronics"))
                .findFirst().orElse(null);
        assertThat(electronicsCount).isNotNull();
        assertThat(electronicsCount.getTotal()).isEqualTo(2L);

        OfferCount booksCount = offerCounts.stream()
                .filter(count -> count.getCategoryName().equals("Books"))
                .findFirst().orElse(null);
        assertThat(booksCount).isNotNull();
        assertThat(booksCount.getTotal()).isEqualTo(1L);
    }

    @Test
    public void SearchOffers_Query_ReturnOfferPage() {
        Offer offer1 = Offer.builder().name("Laptop").description("Powerful laptop").build();
        Offer offer2 = Offer.builder().name("Phone").description("Latest smartphone").build();
        offerRepository.saveAll(Arrays.asList(offer1, offer2));

        Page<Offer> searchResults = offerRepository.searchOffers("Laptop", Pageable.unpaged());

        assertThat(searchResults.getContent()).contains(offer1);
        assertThat(searchResults.getContent()).doesNotContain(offer2);
    }

    @Test
    public void UpdateOffer_ReturnUpdatedOffer() {
        Offer offer = Offer.builder().name("Laptop").description("Great device").price(300.0).build();
        Offer savedOffer = offerRepository.save(offer);

        Offer offerToUpdate = offerRepository.findById(savedOffer.getId()).get();

        offerToUpdate.setName("Phone");
        offerToUpdate.setDescription("Latest smartphone");

        Offer updatedOffer =  offerRepository.save(offerToUpdate);

        assertThat(updatedOffer.getId()).isEqualTo(savedOffer.getId());
        assertThat(updatedOffer.getName()).isEqualTo("Phone");
        assertThat(updatedOffer.getDescription()).isEqualTo("Latest smartphone");
    }

    @Test
    public void DeleteOffer_ReturnEmpty() {
        Offer offer = Offer.builder().name("Laptop").description("Great device").price(300.0).build();
        Offer savedOffer = offerRepository.save(offer);

        offerRepository.deleteById(savedOffer.getId());

        Optional<Offer> deletedOffer = offerRepository.findById(savedOffer.getId());

        assertThat(deletedOffer).isEmpty();
    }
}
