package com.example.marketplace.service.impl;

import com.example.marketplace.dto.OfferCount;
import com.example.marketplace.dto.OfferResponse;
import com.example.marketplace.exception.CategoryNotFoundException;
import com.example.marketplace.exception.OfferNotFoundException;
import com.example.marketplace.exception.UserNotFoundException;
import com.example.marketplace.mapper.OfferMapper;
import com.example.marketplace.model.Category;
import com.example.marketplace.model.User;
import com.example.marketplace.repository.CategoryRepository;
import com.example.marketplace.repository.OfferRepository;
import com.example.marketplace.repository.UserRepository;
import com.example.marketplace.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final OfferMapper offerMapper;

    @Override
    @Transactional(readOnly = true)
    public OfferResponse getOfferById(Long id) {
        return offerRepository.findById(id)
                .map(offerMapper)
                .orElseThrow(() -> new OfferNotFoundException("No offer with ID: " + id.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OfferResponse> getOffers() {
        return offerRepository.findAll()
                .stream().map(offerMapper)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OfferResponse> getOffersByCategoryName(String categoryName) {
        Category category = categoryRepository.findByNameIgnoreCase(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException("No category with name: "+ categoryName.toString()));
        System.out.println(categoryName);
        return offerRepository.findAllByCategoryId(category.getId())
                .stream().map(offerMapper)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OfferResponse> getOffersByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId.toString()));
        return offerRepository.findAllByUserId(userId)
                .stream().map(offerMapper)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OfferCount> getCountOffersByCategory(){
        return offerRepository.countTotalOffersByCategory();
    }
}
