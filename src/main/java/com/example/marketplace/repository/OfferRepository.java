package com.example.marketplace.repository;

import com.example.marketplace.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findAllByCategoryId(Long categoryId);
}
