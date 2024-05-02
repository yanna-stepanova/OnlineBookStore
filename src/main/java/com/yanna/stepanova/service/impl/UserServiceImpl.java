package com.yanna.stepanova.service.impl;

import com.yanna.stepanova.dto.user.UserRegistrationRequestDto;
import com.yanna.stepanova.dto.user.UserResponseDto;
import com.yanna.stepanova.exception.RegistrationException;
import com.yanna.stepanova.mapper.UserMapper;
import com.yanna.stepanova.repository.user.UserRepository;
import com.yanna.stepanova.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepo.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("This user can't be registered");
        }
        return userMapper.toResponseDto(userRepo.save(userMapper.toModel(requestDto)));
    }
}
