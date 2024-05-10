package com.yanna.stepanova.repository.user;

import com.yanna.stepanova.model.Role;
import com.yanna.stepanova.model.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(RoleName roleName);
}
