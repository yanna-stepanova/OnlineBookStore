package com.yanna.stepanova.security;

import com.yanna.stepanova.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws EntityNotFoundException {
        return userRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(
                String.format("Can't find user by such email: %s", email)));
    }
}
