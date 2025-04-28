package com.kat.bookstore.controller;

import com.kat.bookstore.dto.user.UserLoginRequestDto;
import com.kat.bookstore.dto.user.UserLoginResponseDto;
import com.kat.bookstore.dto.user.UserRegistrationRequestDto;
import com.kat.bookstore.dto.user.UserResponseDto;
import com.kat.bookstore.exception.RegistrationException;
import com.kat.bookstore.security.AuthenticationService;
import com.kat.bookstore.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    public UserResponseDto registerUser(
            @RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto loginRequestDto) {
        return authenticationService.isAuthenticated(loginRequestDto);
    }
}
