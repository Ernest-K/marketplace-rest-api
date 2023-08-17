package com.example.marketplace.service.impl;

import com.example.marketplace.model.SecurityUser;
import com.example.marketplace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return new SecurityUser(userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("No user with name: " + username)));
    }
}
