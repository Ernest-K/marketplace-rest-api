package com.example.marketplace.service;

import com.example.marketplace.dto.request.CreateOfferRequest;
import com.example.marketplace.dto.request.UpdateOfferRequest;
import com.example.marketplace.dto.response.OfferCount;
import com.example.marketplace.dto.response.OfferPageResponse;
import com.example.marketplace.dto.response.OfferResponse;
import com.example.marketplace.mapper.OfferMapper;
import com.example.marketplace.model.*;
import com.example.marketplace.repository.CategoryRepository;
import com.example.marketplace.repository.OfferRepository;
import com.example.marketplace.repository.UserRepository;
import com.example.marketplace.service.impl.OfferServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OfferServiceTest {

    @Mock
    private OfferRepository offerRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OfferMapper offerMapper;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private OfferServiceImpl offerService;

    private Offer offer1, offer2;

    @BeforeEach
    public void init() {
        offer1 = Offer.builder()
                .id(1L)
                .name("test")
                .description("test description")
                .price(10.0)
                .build();
        offer2 = Offer.builder()
                .id(2L)
                .name("test")
                .description("test description")
                .price(10.0)
                .build();

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void GetOfferById_ValidId_ReturnOfferResponse() {
        when(offerRepository.findById(anyLong())).thenReturn(Optional.of(offer1));
        when(offerMapper.mapToDto(Mockito.any(Offer.class))).thenReturn(new OfferResponse());

        OfferResponse offerResponse = offerService.getOfferById(offer1.getId());

        assertThat(offerResponse).isNotNull();
    }

    @Test
    public void GetOffers_ReturnOfferPageResponse(){
        Page<Offer> offerPage = Mockito.mock(Page.class);
        when(offerRepository.findAll(Mockito.any(Pageable.class))).thenReturn(offerPage);

        OfferPageResponse offerPageResponse = offerService.getOffers(0, 10, "id", "asc");

        assertThat(offerPageResponse).isNotNull();
    }

    @Test
    public void GetOffersByCategoryName_CategoryName_ReturnOfferPageResponse() {
        Category category = Category.builder().id(1L).name("electronics").build();
        Page<Offer> offerPage = Mockito.mock(Page.class);
        when(categoryRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(category));
        when(offerRepository.findAllByCategoryId(anyLong(), Mockito.any(Pageable.class))).thenReturn(offerPage);

        OfferPageResponse offerPageResponse = offerService.getOffersByCategoryName("electronics", 0, 10, "id", "asc");

        assertThat(offerPageResponse).isNotNull();
    }

    @Test
    public void GetOffersByUserId_ValidUserId_ReturnOfferPageResponse() {
        User user = User.builder()
                .id(1L)
                .username("john_doe")
                .email("john.doe@example.com")
                .password("secretpassword")
                .roles(Set.of(new Role(RoleName.ROLE_USER)))
                .build();
        Page<Offer> offerPage = Mockito.mock(Page.class);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(offerRepository.findAllByUserId(anyLong(), Mockito.any(Pageable.class))).thenReturn(offerPage);

        OfferPageResponse offerPageResponse = offerService.getOffersByUserId(1L, 0, 10, "id", "asc");

        assertThat(offerPageResponse).isNotNull();
    }

    @Test
    public void GetCountOffersByCategory_ReturnOfferCountList() {
        when(offerRepository.countTotalOffersByCategory()).thenReturn(List.of(new OfferCount(1L, "electronics", 1L)));

        List<OfferCount> offerCountList = offerService.getCountOffersByCategory();

        assertThat(offerCountList).isNotNull();
    }

    @Test
    public void CreateOffer_CreateOfferRequest_ReturnOfferResponse() {
        CreateOfferRequest createOfferRequest = new CreateOfferRequest();
        createOfferRequest.setName("Phone");
        createOfferRequest.setDescription("Latest device");
        createOfferRequest.setPrice(250.0);
        createOfferRequest.setCategoryId(1L);

        Category category = Category.builder()
                .id(1L)
                .name("electronics")
                .build();

        Offer offer = Offer.builder()
                .id(1L)
                .name("Phone")
                .description("Latest device")
                .price(250.0)
                .category(category)
                .build();

        User user = User.builder()
                .id(1L)
                .username("john_doe")
                .email("john.doe@example.com")
                .password("secretpassword")
                .roles(Set.of(new Role(RoleName.ROLE_USER)))
                .build();

        SecurityUser securityUser = new SecurityUser(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(offerRepository.save(Mockito.any(Offer.class))).thenReturn(offer);
        when(offerMapper.mapToDto(Mockito.any(Offer.class))).thenReturn(new OfferResponse());

        OfferResponse offerResponse = offerService.createOffer(user.getId(), createOfferRequest);

        verify(offerRepository, times(1)).save(any(Offer.class));
        assertThat(offerResponse).isNotNull();
    }

    @Test
    public void UpdateOffer_UpdateOfferRequest_ReturnOfferResponse() {
        UpdateOfferRequest updateOfferRequest = new UpdateOfferRequest();
        updateOfferRequest.setPrice(220.0);

        Category category = Category.builder()
                .id(1L)
                .name("electronics")
                .build();

        User user = User.builder()
                .id(1L)
                .username("john_doe")
                .email("john.doe@example.com")
                .password("secretpassword")
                .roles(Set.of(new Role(RoleName.ROLE_USER)))
                .build();

        Offer offer = Offer.builder()
                .id(1L)
                .name("Phone")
                .description("Latest device")
                .price(250.0)
                .category(category)
                .user(user)
                .build();

        SecurityUser securityUser = new SecurityUser(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(offerRepository.findById(anyLong())).thenReturn(Optional.of(offer));
        when(offerRepository.save(Mockito.any(Offer.class))).thenReturn(offer);
        when(offerMapper.mapToDto(Mockito.any(Offer.class))).thenReturn(new OfferResponse());

        OfferResponse offerResponse = offerService.updateOffer(user.getId(), offer.getId(), updateOfferRequest);

        verify(offerRepository, times(1)).save(any(Offer.class));
        assertThat(offerResponse).isNotNull();
    }

    @Test
    public void DeleteOffer_ValidOfferId_ReturnVoid() {
        User user = User.builder()
                .id(1L)
                .username("john_doe")
                .email("john.doe@example.com")
                .password("secretpassword")
                .roles(Set.of(new Role(RoleName.ROLE_USER)))
                .build();

        Category category = Category.builder()
                .id(1L)
                .name("electronics")
                .build();

        Offer offer = Offer.builder()
                .id(1L)
                .name("Phone")
                .description("Latest device")
                .price(250.0)
                .category(category)
                .user(user)
                .build();

        SecurityUser securityUser = new SecurityUser(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(offerRepository.findById(anyLong())).thenReturn(Optional.of(offer));

        assertAll(() -> offerService.deleteOffer(user.getId(), offer.getId()));
    }

    @Test
    public void DeleteOffer_InvalidOfferId_ThrowAccessDeniedException() {
        User user = User.builder()
                .id(1L)
                .username("john_doe")
                .email("john.doe@example.com")
                .password("secretpassword")
                .roles(Set.of(new Role(RoleName.ROLE_USER)))
                .build();

        Category category = Category.builder()
                .id(1L)
                .name("electronics")
                .build();

        Offer offer = Offer.builder()
                .id(1L)
                .name("Phone")
                .description("Latest device")
                .price(250.0)
                .category(category)
                .user(user)
                .build();

        SecurityUser securityUser = new SecurityUser(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(offerRepository.findById(anyLong())).thenReturn(Optional.of(offer));

        assertThrows(AccessDeniedException.class, () -> offerService.deleteOffer(2L, offer.getId()));
    }
}
