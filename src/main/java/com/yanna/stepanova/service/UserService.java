package com.yanna.stepanova.service;

import com.yanna.stepanova.dto.user.UserRegistrationRequestDto;
import com.yanna.stepanova.dto.user.UserResponseDto;
import com.yanna.stepanova.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
