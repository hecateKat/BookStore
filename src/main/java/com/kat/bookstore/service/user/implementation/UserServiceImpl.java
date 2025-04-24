package com.kat.bookstore.service.user.implementation;

import com.kat.bookstore.dto.user.UserRegistrationRequestDto;
import com.kat.bookstore.dto.user.UserResponseDto;
import com.kat.bookstore.exception.RegistrationException;
import com.kat.bookstore.mapper.UserMapper;
import com.kat.bookstore.repository.user.UserRepository;
import com.kat.bookstore.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(
            UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new RegistrationException("User with this email already exists");
        }
        return userMapper.toResponseDto(userRepository.save(userMapper.toModel(requestDto)));
    }
}
