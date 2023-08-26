package com.example.marketplace.repository;

import com.example.marketplace.dto.response.OfferCount;
import com.example.marketplace.model.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OfferRepository extends PagingAndSortingRepository<Offer, Long>, CrudRepository<Offer, Long> {

    Page<Offer> findAllByCategoryId(Long categoryId, Pageable pageable);
    
    Page<Offer> findAllByUserId(Long userId, Pageable pageable);

    @Query("SELECT new com.example.marketplace.dto.response.OfferCount(category.id, category.name, COUNT(category)) FROM Offer GROUP BY category")
    List<OfferCount> countTotalOffersByCategory();
}
