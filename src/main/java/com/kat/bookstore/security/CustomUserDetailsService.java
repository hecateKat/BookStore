package com.kat.bookstore.security;

import com.kat.bookstore.exception.EntityNotFoundException;
import com.kat.bookstore.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws EntityNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "User with email " + email + " not found"));
    }
}
