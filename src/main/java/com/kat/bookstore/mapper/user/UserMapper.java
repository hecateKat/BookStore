package com.kat.bookstore.mapper.user;

import com.kat.bookstore.config.MapperConfig;
import com.kat.bookstore.dto.user.UserRegistrationRequestDto;
import com.kat.bookstore.dto.user.UserResponseDto;
import com.kat.bookstore.entity.user.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    User toModel(UserRegistrationRequestDto requestDto);

    UserResponseDto toResponseDto(User user);
}
