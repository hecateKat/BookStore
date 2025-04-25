package com.kat.bookstore.security;

import com.kat.bookstore.dto.user.UserLoginRequestDto;
import com.kat.bookstore.entity.user.User;
import com.kat.bookstore.repository.user.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    public boolean isAuthenticated(UserLoginRequestDto loginRequestDto) {
        Optional<User> user = userRepository.findByEmail((loginRequestDto.email()));
        return user.isPresent() && user.get().getPassword().equals(loginRequestDto.password());
    }
}
