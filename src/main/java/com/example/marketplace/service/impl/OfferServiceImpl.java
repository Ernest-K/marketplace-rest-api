package com.example.marketplace.service.impl;

import com.example.marketplace.dto.OfferCount;
import com.example.marketplace.dto.OfferPageResponse;
import com.example.marketplace.dto.OfferResponse;
import com.example.marketplace.exception.CategoryNotFoundException;
import com.example.marketplace.exception.OfferNotFoundException;
import com.example.marketplace.exception.UserNotFoundException;
import com.example.marketplace.mapper.OfferMapper;
import com.example.marketplace.model.Category;
import com.example.marketplace.model.Offer;
import com.example.marketplace.model.User;
import com.example.marketplace.repository.CategoryRepository;
import com.example.marketplace.repository.OfferRepository;
import com.example.marketplace.repository.UserRepository;
import com.example.marketplace.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
                .orElseThrow(() -> new OfferNotFoundException("No offer with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public OfferPageResponse getOffers(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Offer> offerPage = offerRepository.findAll(pageable);

        return buildOfferPageResponse(offerPage);
    }

    @Override
    @Transactional(readOnly = true)
    public OfferPageResponse getOffersByCategoryName(String categoryName, Integer pageNo, Integer pageSize) {
        Category category = categoryRepository.findByNameIgnoreCase(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException("No category with name: "+ categoryName));

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Offer> offerPage = offerRepository.findAllByCategoryId(category.getId(), pageable);

        return buildOfferPageResponse(offerPage);
    }

    @Override
    @Transactional(readOnly = true)
    public OfferPageResponse getOffersByUserId(Long userId, Integer pageNo, Integer pageSize) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("No user with id: " + userId));

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Offer> offerPage = offerRepository.findAllByUserId(user.getId(), pageable);

        return buildOfferPageResponse(offerPage);
    }

    @Transactional(readOnly = true)
    public List<OfferCount> getCountOffersByCategory(){
        return offerRepository.countTotalOffersByCategory();
    }

    private OfferPageResponse buildOfferPageResponse(Page<Offer> offerPage){
        List<OfferResponse> offerResponses = offerPage.getContent().stream().map(offerMapper).toList();

        OfferPageResponse offerPageResponse = new OfferPageResponse();
        offerPageResponse.setOfferResponses(offerResponses);
        offerPageResponse.setPageNo(offerPage.getNumber());
        offerPageResponse.setPageSize(offerPage.getSize());
        offerPageResponse.setTotalPages(offerPage.getTotalPages());
        offerPageResponse.setTotalElements(offerPage.getTotalElements());
        offerPageResponse.setLast(offerPage.isLast());

        return offerPageResponse;
    }
}
