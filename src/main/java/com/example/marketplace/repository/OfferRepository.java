package com.example.marketplace.repository;

import com.example.marketplace.dto.response.OfferCount;
import com.example.marketplace.model.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OfferRepository extends PagingAndSortingRepository<Offer, Long>, JpaRepository<Offer, Long> {

    Page<Offer> findAllByCategoryId(Long categoryId, Pageable pageable);
    
    Page<Offer> findAllByUserId(Long userId, Pageable pageable);

    @Query("SELECT new com.example.marketplace.dto.response.OfferCount(category.id, category.name, COUNT(category)) FROM Offer GROUP BY category")
    List<OfferCount> countTotalOffersByCategory();

    @Query("SELECT o FROM Offer o WHERE " +
            "LOWER(o.name) LIKE LOWER(CONCAT('%', :query, '%'))" +
            "Or LOWER(o.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Offer> searchOffers(@Param("query") String query, Pageable pageable);
}
