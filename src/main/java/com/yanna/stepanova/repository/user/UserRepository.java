package com.yanna.stepanova.repository.user;

import com.yanna.stepanova.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);

    Optional<UserDetails> findByEmail(String email);
}
