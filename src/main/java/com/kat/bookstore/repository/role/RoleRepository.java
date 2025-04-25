package com.kat.bookstore.repository.role;

import com.kat.bookstore.entity.role.Role;
import com.kat.bookstore.entity.role.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
