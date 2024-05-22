package com.yanna.stepanova.service;

import com.yanna.stepanova.dto.user.UserRegistrationRequestDto;
import com.yanna.stepanova.dto.user.UserResponseDto;
import com.yanna.stepanova.exception.RegistrationException;
import com.yanna.stepanova.model.User;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;

    User getAuthenticatedUser();
}
