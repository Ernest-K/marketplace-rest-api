package com.example.marketplace.repository;

import com.example.marketplace.dto.OfferCount;
import com.example.marketplace.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findAllByCategoryId(Long categoryId);
    List<Offer> findAllByUserId(Long userId);

    @Query("SELECT new com.example.marketplace.dto.OfferCount(category.id, category.name, COUNT(category)) FROM Offer GROUP BY category")
    List<OfferCount> countTotalOffersByCategory();
}
