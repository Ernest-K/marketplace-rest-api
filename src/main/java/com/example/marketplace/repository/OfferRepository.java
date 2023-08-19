package com.example.marketplace.repository;

import com.example.marketplace.dto.OfferCount;
import com.example.marketplace.model.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    Page<Offer> findAllByCategoryId(Long categoryId, Pageable pageable);
    Page<Offer> findAllByUserId(Long userId, Pageable pageable);

    @Query("SELECT new com.example.marketplace.dto.OfferCount(category.id, category.name, COUNT(category)) FROM Offer GROUP BY category")
    List<OfferCount> countTotalOffersByCategory();
}
