package com.example.marketplace.service.impl;

import com.example.marketplace.dto.request.OfferRequest;
import com.example.marketplace.dto.response.OfferCount;
import com.example.marketplace.dto.response.OfferPageResponse;
import com.example.marketplace.dto.response.OfferResponse;
import com.example.marketplace.exception.CategoryNotFoundException;
import com.example.marketplace.exception.OfferNotFoundException;
import com.example.marketplace.exception.UserNotFoundException;
import com.example.marketplace.mapper.OfferMapper;
import com.example.marketplace.model.Category;
import com.example.marketplace.model.Offer;
import com.example.marketplace.model.SecurityUser;
import com.example.marketplace.model.User;
import com.example.marketplace.repository.CategoryRepository;
import com.example.marketplace.repository.OfferRepository;
import com.example.marketplace.repository.UserRepository;
import com.example.marketplace.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final OfferMapper offerMapper;

    @Override
    @Transactional(readOnly = true)
    public OfferResponse getOfferById(Long offerId) {
        return offerRepository.findById(offerId)
                .map(offerMapper::mapToDto)
                .orElseThrow(() -> new OfferNotFoundException("No offer with id: " + offerId));
    }

    @Override
    @Transactional(readOnly = true)
    public OfferPageResponse getOffers(Integer pageNo, Integer pageSize, String sortBy, String direction) {
        if(!areSortParametersValid(sortBy, direction)){
            throw new InvalidParameterException("Sorting parameter not valid");
        }

        Order order = new Order(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(order));
        Page<Offer> offerPage = offerRepository.findAll(pageable);

        return buildOfferPageResponse(offerPage);
    }

    @Override
    @Transactional(readOnly = true)
    public OfferPageResponse getOffersByCategoryName(String categoryName, Integer pageNo, Integer pageSize, String sortBy, String direction) {
        Category category = categoryRepository.findByNameIgnoreCase(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException("No category with name: "+ categoryName));

        if(!areSortParametersValid(sortBy, direction)){
            throw new InvalidParameterException("Sorting parameter not valid");
        }

        Order order = new Order(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(order));

        Page<Offer> offerPage = offerRepository.findAllByCategoryId(category.getId(), pageable);

        return buildOfferPageResponse(offerPage);
    }

    @Override
    @Transactional(readOnly = true)
    public OfferPageResponse getOffersByUserId(Long userId, Integer pageNo, Integer pageSize, String sortBy, String direction) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("No user with id: " + userId));

        if(!areSortParametersValid(sortBy, direction)){
            throw new InvalidParameterException("Sorting parameter not valid");
        }

        Order order = new Order(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(order));

        Page<Offer> offerPage = offerRepository.findAllByUserId(user.getId(), pageable);

        return buildOfferPageResponse(offerPage);
    }

    @Transactional(readOnly = true)
    public List<OfferCount> getCountOffersByCategory(){
        return offerRepository.countTotalOffersByCategory();
    }

    @Override
    @Transactional
    public OfferResponse createOffer(Long userId, OfferRequest offerRequest) {
        if(!hasUserAccess(userId)){
            throw new AccessDeniedException("Not allowed to create offer");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("No user with id: " + userId));

        Category category = categoryRepository.findById(offerRequest.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("No category with id: " + offerRequest.getCategoryId()));

        Offer offer = new Offer();
        offer.setName(offerRequest.getName());
        offer.setDescription(offerRequest.getDescription());
        offer.setPrice(offerRequest.getPrice());
        offer.setUser(user);
        offer.setCategory(category);

        return offerMapper.mapToDto(offerRepository.save(offer));
    }

    @Override
    @Transactional
    public OfferResponse updateOffer(Long userId, Long offerId, OfferRequest offerRequest) {
        Offer offerToUpdate = offerRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException("No offer with id: " + offerId));

        if(!hasUserAccess(userId) || !userId.equals(offerToUpdate.getUser().getId())){
            throw new AccessDeniedException("Not allowed to update offer");
        }

        Map<String, Object> dtoMap = convertDtoToMap(offerRequest);

        for (Map.Entry<String, Object> entry : dtoMap.entrySet()) {
            String fieldName = entry.getKey();
            Object newValue = entry.getValue();
            if (newValue != null){
                if (fieldName.equals("categoryId")){
                    Category newCategory = categoryRepository.findById((Long) newValue)
                            .orElseThrow(() -> new CategoryNotFoundException("No category with id: " + newValue));
                    updateFieldInEntity(offerToUpdate, "category", newCategory);
                }else{
                    updateFieldInEntity(offerToUpdate, fieldName, newValue);
                }
            }
        }

        return offerMapper.mapToDto(offerRepository.save(offerToUpdate));
    }

    @Override
    @Transactional
    public void deleteOffer(Long userId, Long offerId) {
        Offer offerToDelete = offerRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException("No offer with id: " + offerId));

        if(!hasUserAccess(userId) || !userId.equals(offerToDelete.getUser().getId())){
            throw new AccessDeniedException("Not allowed to delete offer");
        }

        offerRepository.delete(offerToDelete);
    }

    @Override
    @Transactional
    public OfferPageResponse searchOffers(String query, Integer pageNo, Integer pageSize, String sortBy, String direction){
        if(!areSortParametersValid(sortBy, direction)){
            throw new InvalidParameterException("Sorting parameter not valid");
        }

        Order order = new Order(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(order));

        Page<Offer> offerPage = offerRepository.searchOffers(query, pageable);

        return buildOfferPageResponse(offerPage);
    }

    private OfferPageResponse buildOfferPageResponse(Page<Offer> offerPage){
        List<OfferResponse> offerResponses = offerPage.getContent().stream().map(offerMapper::mapToDto).toList();

        OfferPageResponse offerPageResponse = new OfferPageResponse();
        offerPageResponse.setOfferResponses(offerResponses);
        offerPageResponse.setPageNo(offerPage.getNumber());
        offerPageResponse.setPageSize(offerPage.getSize());
        offerPageResponse.setTotalPages(offerPage.getTotalPages());
        offerPageResponse.setTotalElements(offerPage.getTotalElements());
        offerPageResponse.setLast(offerPage.isLast());

        return offerPageResponse;
    }

    private Map<String, Object> convertDtoToMap(OfferRequest offerRequest){
        Map<String, Object> dtoMap = new HashMap<>();

        Field[] fields = offerRequest.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(offerRequest);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            dtoMap.put(field.getName(), value);
        }

        return dtoMap;
    }

    private void updateFieldInEntity(Offer offer, String fieldName, Object newValue) {
        try {
            Field field = offer.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(offer, newValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean hasUserAccess(Long userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser authenticatedSecurityUser = (SecurityUser) authentication.getPrincipal();

        return userId.equals(authenticatedSecurityUser.getId());
    }

    private boolean areSortParametersValid(String sortBy, String direction){
        Field[] declaredFields = OfferResponse.class.getDeclaredFields();
        List<String> declaredFieldsName = Arrays.stream(declaredFields).map(Field::getName).toList();

        List<String> directionsName = Arrays.stream(Sort.Direction.values()).map(Enum::toString).toList();

        return declaredFieldsName.contains(sortBy) && directionsName.contains(direction.toUpperCase());
    }
}
