package com.kat.bookstore.service.user;

import com.kat.bookstore.dto.user.UserRegistrationRequestDto;
import com.kat.bookstore.dto.user.UserResponseDto;
import com.kat.bookstore.exception.RegistrationException;

public interface UserService {

    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
