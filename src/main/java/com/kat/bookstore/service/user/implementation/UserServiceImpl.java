package com.kat.bookstore.service.user.implementation;

import com.kat.bookstore.dto.user.UserRegistrationRequestDto;
import com.kat.bookstore.dto.user.UserResponseDto;
import com.kat.bookstore.entity.user.User;
import com.kat.bookstore.exception.RegistrationException;
import com.kat.bookstore.mapper.user.UserMapper;
import com.kat.bookstore.repository.user.UserRepository;
import com.kat.bookstore.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(
            UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new RegistrationException("User with this email already exists");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        user.setEmail(requestDto.email());
        User savedUser = userRepository.save(user);
        return userMapper.toResponseDto(savedUser);
    }
}
