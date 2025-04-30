package com.kat.bookstore.service.user.implementation;

import com.kat.bookstore.dto.user.UserRegistrationRequestDto;
import com.kat.bookstore.dto.user.UserResponseDto;
import com.kat.bookstore.entity.role.Role;
import com.kat.bookstore.entity.role.RoleName;
import com.kat.bookstore.entity.shoppingcart.ShoppingCart;
import com.kat.bookstore.entity.user.User;
import com.kat.bookstore.exception.EntityNotFoundException;
import com.kat.bookstore.exception.RegistrationException;
import com.kat.bookstore.mapper.shoppingcart.ShoppingCartMapper;
import com.kat.bookstore.mapper.user.UserMapper;
import com.kat.bookstore.repository.role.RoleRepository;
import com.kat.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.kat.bookstore.repository.user.UserRepository;
import com.kat.bookstore.service.user.UserService;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_ROLE = "ROLE_USER";
    private final UserRepository userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepo;
    private final ShoppingCartRepository shopCartRepo;
    private final ShoppingCartMapper shopCartMapper;

    @Override
    @Transactional
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepo.existsByEmail(requestDto.email())) {
            throw new RegistrationException("This user can't be registered");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(generateDefaultSetRoles());
        User savedUser = userRepo.save(user);
        if (!shopCartRepo.existsById(savedUser.getId())) {
            ShoppingCart shoppingCart = shopCartMapper.mapUserToShopCart(savedUser);
            shopCartRepo.save(shoppingCart);
        }
        return userMapper.toResponseDto(savedUser);
    }

    private Set<Role> generateDefaultSetRoles() {
        Role roleFromDB = roleRepo.findByName(RoleName.getByType(DEFAULT_ROLE))
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Can't find %s in table roles: ", DEFAULT_ROLE)));
        Set<Role> roles = new HashSet<>();
        roles.add(roleFromDB);
        return roles;
    }
}
