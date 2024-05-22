package com.yanna.stepanova.service.impl;

import com.yanna.stepanova.dto.user.UserRegistrationRequestDto;
import com.yanna.stepanova.dto.user.UserResponseDto;
import com.yanna.stepanova.exception.RegistrationException;
import com.yanna.stepanova.mapper.ShoppingCartMapper;
import com.yanna.stepanova.mapper.UserMapper;
import com.yanna.stepanova.model.Role;
import com.yanna.stepanova.model.RoleName;
import com.yanna.stepanova.model.ShoppingCart;
import com.yanna.stepanova.model.User;
import com.yanna.stepanova.repository.shoppingcart.ShoppingCartRepository;
import com.yanna.stepanova.repository.user.RoleRepository;
import com.yanna.stepanova.repository.user.UserRepository;
import com.yanna.stepanova.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
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

    @Override
    public User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return (User) userRepo.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException("Can't find user by email: " + email));
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
